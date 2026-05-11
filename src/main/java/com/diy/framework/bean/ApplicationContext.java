package com.diy.framework.bean;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

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

    public <T> T getBean(final Class<T> clazz) {
        return beans.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Bean not found"));
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

    private boolean isBeanInitialized(Class<?> beanClass) {
        return beans.stream().anyMatch(bean -> beanClass.getClass().equals(beanClass));
    }

    private void saveBean(Object bean) {
        beans.add(bean);
    }
}
