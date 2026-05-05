package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.Autowired;
import com.diy.framework.web.annotations.Component;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanContainer {

  private static final Map<Class<?>, Object> BEAN_CONTAINER = new HashMap<>();

  public BeanContainer(String basePackages) {
    Set<Class<?>> classes = new BeanScanner(basePackages)
        .scanClassesTypeAnnotatedWith(Component.class);

    for (Class<?> clazz : classes) {
      createBean(clazz);
    }
  }

  public <T> T getBean(Class<T> clazz) {
    return (T) BEAN_CONTAINER.get(clazz);
  }

  private Object createBean(Class<?> clazz) {
    if (BEAN_CONTAINER.containsKey(clazz)) {
      return BEAN_CONTAINER.get(clazz);
    }

    Constructor<?> constructor = resolveConstructor(clazz);
    int paramCount = constructor.getParameterCount();

    try {
      if (paramCount == 0) {
        Object newInstance = constructor.newInstance();
        BEAN_CONTAINER.put(clazz, constructor.newInstance());
      }

      Object[] params = createConstructorParams(constructor);
      BEAN_CONTAINER.put(clazz, constructor.newInstance(params));
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    return BEAN_CONTAINER.get(clazz);
  }

  private Object[] createConstructorParams(Constructor<?> constructor) {
    Object[] params = new Object[constructor.getParameterCount()];

    for (int i = 0; i < constructor.getParameterCount(); i++) {
      Class<?> param = constructor.getParameterTypes()[i];
      if (!BEAN_CONTAINER.containsKey(param)) {
        createBean(param);
      }
      params[i] = BEAN_CONTAINER.get(param);
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

}
