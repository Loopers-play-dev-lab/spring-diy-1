package com.diy.framework.web.beans.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(String... basePackages) throws Exception{
        BeanScanner scanner = new BeanScanner(basePackages);
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);

        for(Class<?> clazz :  classes) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            beans.put(clazz, instance);
        }
    }

}
