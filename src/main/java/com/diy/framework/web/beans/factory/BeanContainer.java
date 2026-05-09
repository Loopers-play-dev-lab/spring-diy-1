package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.annotations.Autowired;
import com.diy.framework.web.beans.annotations.Component;
import com.diy.framework.web.server.exceptions.CannotCreateBeanException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanContainer {

  private final Set<Class<?>> beanClasses = new HashSet<>();
  private final Map<Class<?>, Object> beans = new HashMap<>();

  public BeanContainer(String basePackages) {
    beanClasses.addAll(
        new BeanScanner(basePackages)
            .scanClassesTypeAnnotatedWith(Component.class)
    );

    for (Class<?> clazz : beanClasses) {
      if (isBeanInitialized(clazz)) continue;
      createBean(clazz);
    }
  }

  public <T> T getBean(Class<T> clazz) {
    return (T) beans.get(clazz);
  }

  private Object createBean(Class<?> clazz) {
    if (beans.containsKey(clazz)) {
      return beans.get(clazz);
    }

    Constructor<?> constructor = resolveConstructor(clazz);
    int paramCount = constructor.getParameterCount();

    try {
      if (paramCount == 0) {
        beans.put(clazz, constructor.newInstance());
      }

      validateConstructorParams(constructor);
      Object[] params = createConstructorParams(constructor);
      beans.put(clazz, constructor.newInstance(params));
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    return beans.get(clazz);
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
      Class<?> param = constructor.getParameterTypes()[i];
      if (!beans.containsKey(param)) {
        createBean(param);
      }
      params[i] = beans.get(param);
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
    return beans.containsKey(parameterType);
  }
}
