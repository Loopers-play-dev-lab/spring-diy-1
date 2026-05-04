package com.diy.framework.web.beans.factory;

import com.diy.framework.annotation.Autowired;
import com.diy.framework.annotation.Component;
import java.lang.reflect.Constructor;
import java.util.Arrays;
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

    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    private void registerBean(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return;
        }

        try {
            Constructor<?> constructor = resolveConstructor(clazz);

            Object[] args = Arrays.stream(constructor.getParameterTypes())
                    .map(type -> {
                        registerBean(type);
                        return beans.get(type);
                    })
                    .toArray();

            beans.put(clazz, constructor.newInstance(args));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> resolveConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElseGet(() -> clazz.getDeclaredConstructors()[0]);
    }
}
