package com.diy.framework.context;

import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApplicationContext {

    private final String basePackage;
    private final Set<Class<?>> beanClasses = new HashSet<>();
    private final List<Object> beans = new ArrayList<>();

    public ApplicationContext(final String basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final BeanScanner beanScanner = new BeanScanner(basePackage);
        beanClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Component.class));

        beanClasses.forEach(clazz -> {
            if (isBeanInitialized(clazz)) {
                return;
            }

            final Object bean = createInstance(clazz);
            saveBean(bean);
        });
    }

    private Object createInstance(final Class<?> clazz) {
        final Constructor<?> constructor = findConstructor(clazz);

        try {
            constructor.setAccessible(true);
            final Object[] parameters = getConstructorParameters(constructor);

            return constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private Constructor<?> findConstructor(final Class<?> clazz) {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return findAutowiredConstructor(constructors);
    }

    private Constructor<?> findAutowiredConstructor(final Constructor<?>[] constructors) {
        final Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if (autowiredConstructors.length != 1) {
            throw new RuntimeException("Autowired constructor not found");
        }

        return autowiredConstructors[0];
    }

    private Object[] getConstructorParameters(final Constructor<?> constructor) {
        final List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes()).toList();

        if (!beanClasses.containsAll(parameterTypes)) {
            throw new RuntimeException("parameter is not bean");
        }

        return parameterTypes.stream().map(parameterType -> {
            if (isBeanInitialized(parameterType)) {
                return beans.stream().findFirst().get();
            }

            final Object bean = createInstance(parameterType);
            saveBean(bean);

            return bean;
        }).toArray();
    }

    private boolean isBeanInitialized(final Class<?> parameterType) {
        return beans.stream().anyMatch(bean -> bean.getClass().equals(parameterType));
    }

    private void saveBean(final Object bean) {
        beans.add(bean);
    }
}
