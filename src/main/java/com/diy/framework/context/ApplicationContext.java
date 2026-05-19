package com.diy.framework.context;

import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ApplicationContext {
    private final Class<?> basePackageClass;
    private final Set<Class<?>> beanClasses = new HashSet<>();
    private final List<Object> beans = new ArrayList<>();

    public ApplicationContext(final Class<?> basePackageClass) {
        this.basePackageClass = basePackageClass;
    }

    public void initialize() {
        final BeanScanner beanScanner = new BeanScanner(basePackageClass.getPackageName());
        final Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        beanClasses.addAll(classes);

        for (Class<?> beanClass : beanClasses) {
            if (isInitialized(beanClass)) {
                return;
            }
            final Object bean = createInstance(beanClass);
            saveBean(bean);
        }
    }

    private Object createInstance(final Class<?> beanClass) {
        final Constructor<?> constructor = findConstructor(beanClass);

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

    private Constructor<?> findConstructor(final Class<?> beanClass) {
        final Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
        if (constructors.length == 1) {
            return constructors[0];
        }
        return findAutowiredConstructor(constructors);
    }

    private static Constructor<?> findAutowiredConstructor(final Constructor<?>[] constructors) {
        final Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                                                             .filter(c -> c.isAnnotationPresent(Autowired.class))
                                                             .toArray(Constructor[]::new);

        if (autowiredConstructors.length != 1) {
            throw new RuntimeException("Autowired constructor must exactly one constructor");
        }
        return autowiredConstructors[0];
    }

    private Object[] getConstructorParameters(final Constructor<?> constructor) {
        final List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes()).toList();

        if (!beanClasses.containsAll(parameterTypes)) {
            throw new RuntimeException("parameter types must exactly one class");
        }

        return parameterTypes.stream()
                             .map(parameterType -> {
                                 if (isInitialized(parameterType)) {
                                     return getBean(parameterType);
                                 }
                                 final Object bean = createInstance(parameterType);
                                 saveBean(bean);
                                 return bean;
                             })
                             .filter(Objects::nonNull)
                             .toArray();
    }

    private boolean isInitialized(final Class<?> beanClass) {
        return beans.stream().anyMatch(bean -> bean.getClass().equals(beanClass));
    }

    private void saveBean(final Object bean) {
        beans.add(bean);
    }

    public Object getBean(final Class<?> beanClass) {
        return beans.stream()
                    .filter(bean -> bean.getClass().equals(beanClass))
                    .findFirst()
                    .orElse(null);
    }
}
