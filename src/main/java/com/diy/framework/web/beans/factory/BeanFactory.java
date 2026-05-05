package com.diy.framework.web.beans.factory;

import com.diy.framework.annotation.Autowired;
import com.diy.framework.annotation.Component;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Set<Class<?>> componentClasses;

    public BeanFactory(String... basePackages) {
        BeanScanner scanner = new BeanScanner(basePackages);
        componentClasses = scanner.scanClassesTypeAnnotatedWith(Component.class);
        componentClasses.forEach(this::registerBean);
    }

    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    private void registerBean(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return;
        }

        if (clazz.isInterface()) {
            Class<?> implementClass = resolveImplementation(clazz);
            registerBean(implementClass);
            return;
        }

        try {
            Constructor<?> constructor = resolveConstructor(clazz);

            Object[] args = Arrays.stream(constructor.getParameterTypes())
                    .map(type -> {
                        registerBean(type);
                        Object bean = beans.get(type);
                        if(bean == null){
                            throw new RuntimeException("No Search bean");
                        }
                        return bean;
                    })
                    .toArray();

            Object instance = constructor.newInstance(args);
            beans.put(clazz, instance);
            Arrays.stream(clazz.getInterfaces()).forEach(i -> beans.put(i, instance));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?> resolveImplementation(Class<?> interfaceType) {
        List<Class<?>> implementations = componentClasses.stream()
                .filter(interfaceType::isAssignableFrom)
                .toList();

        if (implementations.isEmpty()) {
            throw new RuntimeException("No implementation");
        }
        if (implementations.size() > 1) {
            throw new RuntimeException("Multiple implementations");
        }

        return implementations.getFirst();
    }

    private Constructor<?> resolveConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElseGet(() -> clazz.getDeclaredConstructors()[0]);
    }
}
