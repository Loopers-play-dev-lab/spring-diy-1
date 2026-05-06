package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.Autowired;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public void createBeans(final Set<Class<?>> beanClasses) {
        for (Class<?> clazz : beanClasses) {
            createBean(clazz);
        }
    }

    private Object createBean(final Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        try {
            final Constructor<?> constructor = findConstructor(clazz);
            final Object[] params = resolveParameters(constructor);
            final Object instance = constructor.newInstance(params);
            beans.put(clazz, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("빈 생성 실패: " + clazz.getName(), e);
        }
    }

    private Constructor<?> findConstructor(final Class<?> clazz) {
        // @Autowired가 붙은 생성자 우선
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return constructor;
            }
        }
        // 없으면 기본 생성자
        return clazz.getDeclaredConstructors()[0];
    }

    private Object[] resolveParameters(final Constructor<?> constructor) {
        final Class<?>[] paramTypes = constructor.getParameterTypes();
        final Object[] params = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            // 이미 생성된 빈에서 찾거나, 없으면 생성
            params[i] = beans.containsKey(paramTypes[i])
                    ? beans.get(paramTypes[i])
                    : createBean(paramTypes[i]);
        }

        return params;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T) beans.get(clazz);
    }
}
