package com.diy.framework.beans.factory;

import com.diy.framework.context.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class AnnotatedGenericBeanDefinition implements BeanDefinition {

    private final Class<?> beanClass;
    private final String beanName;
    private final Constructor<?> constructor;

    public AnnotatedGenericBeanDefinition(final Class<?> beanClass) {
        this.beanClass = beanClass;
        this.beanName = beanClass.getSimpleName();
        this.constructor = findConstructor(beanClass);
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public Constructor<?> getFactoryMethod() {
        return this.constructor;
    }

    @Override
    public String getFactoryBeanName() {
        return null;
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.stream(this.constructor.getParameterTypes()).toList();
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
}
