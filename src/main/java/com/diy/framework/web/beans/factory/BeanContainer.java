package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.annotations.Autowired;
import com.diy.framework.web.beans.annotations.Bean;
import com.diy.framework.web.beans.annotations.Component;
import com.diy.framework.web.beans.factory.metadata.BeanMetadata;
import com.diy.framework.web.beans.factory.metadata.ComponentBeanMetadata;
import com.diy.framework.web.beans.factory.metadata.MethodBeanMetadata;
import com.diy.framework.web.server.exceptions.BeanNameConflictException;
import com.diy.framework.web.server.exceptions.CannotCreateBeanException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanContainer {

  private final String basePackage;

  private final Map<String, BeanMetadata> nameMetadata = new HashMap<>();
  private final Set<Class<?>> beanClasses = new HashSet<>();
  private final Map<String, Object> beans = new HashMap<>();

  public BeanContainer(String basePackage) {
    this.basePackage = basePackage;
  }

  public void init() {
    scanMetadata();
    for (BeanMetadata metadata : nameMetadata.values()) {
      createBean(metadata);
    }
  }

  public void scanMetadata() {
    Set<Class<?>> componentClasses = new BeanScanner(basePackage).scanClassesTypeAnnotatedWith(Component.class);

    for (Class<?> clazz : componentClasses) {
      ComponentBeanMetadata metadata = new ComponentBeanMetadata(
          clazz.getSimpleName(),
          resolveConstructor(clazz)
      );
      createMetadata(metadata);

      for (Method method : clazz.getMethods()) {
        if (!method.isAnnotationPresent(Bean.class)) continue;
        createMetadata(MethodBeanMetadata.from(method));
      }
    }

  }

  private void createMetadata(BeanMetadata metadata) {
    if (nameMetadata.containsKey(metadata.name())) {
      throw new BeanNameConflictException(metadata.name());
    }
    nameMetadata.put(metadata.name(), metadata);
  }

  public <T> T getBean(Class<T> clazz) {
    return (T) beans.get(clazz.getSimpleName());
  }

  public <T> T getBean(String name) {
    return (T) beans.get(name);
  }

  public <T> T getBean(Class<T> clazz, String name) {
    if (beans.containsKey(clazz.getSimpleName())) {
      return (T) beans.get(clazz.getSimpleName());
    }
    if (beans.containsKey(name)) {
      return (T) beans.get(name);
    }
    return null;
  }

  public <T> T putBean(String key, T value) {
    Object bean = beans.putIfAbsent(key, value);
    if (bean != null) {
      throw new BeanNameConflictException(key);
    }
    return (T) beans.get(key);
  }

  private Object createBean(BeanMetadata metadata) {
    if (beans.containsKey(metadata.name())) {
      return beans.get(metadata.name());
    }

    Constructor<?> constructor = resolveConstructor(metadata.target());

    try {
      validateConstructorParams(constructor);
      Object[] params = createConstructorParams(constructor);
      Object instance = params.length == 0 ? constructor.newInstance() : constructor.newInstance(params);
      putBean(metadata.name(), instance);

      for (Method method : metadata.target().getMethods()) {
        if (!method.isAnnotationPresent(Bean.class)) continue;
        String beanName = method.getAnnotation(Bean.class).value().isBlank()
            ? method.getReturnType().getSimpleName()
            : method.getAnnotation(Bean.class).value();
        putBean(beanName, method.invoke(instance));
      }
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    return beans.get(metadata.name());
  }

  private void validateConstructorParams(Constructor<?> constructor) {
    for (int i = 0; i < constructor.getParameterCount(); i++) {
      if (!beanClasses.contains(constructor.getParameterTypes()[i])) {
        throw new CannotCreateBeanException(constructor, i);
      }
    }
  }

  private Object[] createConstructorParams(Constructor<?> constructor) {
    Object[] params = new Object[constructor.getParameterCount()];

    for (int i = 0; i < constructor.getParameterCount(); i++) {
      Parameter param = constructor.getParameters()[i];
      Class<?> type = param.getType();
      String name = param.getName();

      if (!beans.containsKey(type.getSimpleName())) {
        createBean(type);
      }

      if (beans.containsKey(type.getSimpleName())) {
        params[i] = beans.get(type.getSimpleName());
      } else if (beans.containsKey(name)) {
        params[i] = beans.get(type.getSimpleName());
      } else {
        createBean(type);
      }

    }

    return params;
  }

  private Constructor<?> resolveConstructor(Class<?> clazz) {
    int maxParamCount = 0;
    int maxIndex = 0;
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();

    for (int i = 0; i < constructors.length; i++) {
      // Autowired 생성자가 1순위
      if (constructors[i].isAnnotationPresent(Autowired.class)) {
        maxIndex = i;
        break;
      }

      // 파라미터가 가장 많은 생성자가 2순위
      if (constructors[i].getParameterCount() > maxParamCount) {
        maxParamCount = constructors[i].getParameterCount();
        maxIndex = i;
      }
    }

    return clazz.getDeclaredConstructors()[maxIndex];
  }

  private boolean isBeanInitialized(Class<?> parameterType) {
    return beans.containsKey(parameterType.getSimpleName());
  }
}
