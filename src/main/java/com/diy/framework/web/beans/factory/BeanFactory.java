package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.Autowired;
import com.diy.framework.web.beans.Component;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final BeanScanner beanScanner;
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(String... basePackages) {
        this.beanScanner = new BeanScanner(basePackages);
        createBeans();
    }

    private void createBeans() {
        Set<Class<?>> beanClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        for (Class<?> clazz : beanClasses) {
            createBean(clazz);
        }
    }

    private Object createBean(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        Constructor<?> constructor = findConstructor(clazz);
        Object instance;

        try {
            if (constructor.getParameterCount() == 0) {
                instance = constructor.newInstance();
            } else {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = getBean(paramTypes[i]);
                }
                instance = constructor.newInstance(params);
            }
        } catch (Exception e) {
            throw new RuntimeException("빈 생성 실패: " + clazz.getName(), e);
        }

        beans.put(clazz, instance);
        return instance;
    }

    public Object getBean(Class<?> paramType) {
        for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
            if (paramType.isAssignableFrom(entry.getKey())) {
                return entry.getValue();
            }
        }

        Set<Class<?>> beanClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        for (Class<?> clazz : beanClasses) {
            if (paramType.isAssignableFrom(clazz)) {
                return createBean(clazz);
            }
        }

        throw new RuntimeException("빈을 찾을 수 없습니다: " + paramType.getName());
    }

    public Constructor<?> findConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElseGet(() -> {
                    try {
                        return clazz.getDeclaredConstructor();
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("적절한 생성자를 찾을 수 없습니다: " + clazz.getName(), e);
                    }
                });
    }
}