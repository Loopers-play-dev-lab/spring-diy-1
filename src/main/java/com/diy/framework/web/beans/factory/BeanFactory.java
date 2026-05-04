package com.diy.framework.web.beans.factory;

import com.diy.framework.annotation.Component;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(String... basePackages) {
        BeanScanner scanner = new BeanScanner(basePackages);
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);
        classes.forEach(this::registerBean);
    }

    private void registerBean(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            beans.put(clazz, instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
