package com.diy.framework.web.context;

import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.context.annotation.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public ApplicationContext(final String... basePackages) throws Exception {
        BeanScanner scanner = new BeanScanner(basePackages);
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> clazz : classes) {
            Object bean = clazz.getDeclaredConstructor().newInstance();
            beans.put(clazz, bean);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }
}
