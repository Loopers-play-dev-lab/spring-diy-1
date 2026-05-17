package com.diy.framework.web.beans.factory;

import com.diy.framework.web.context.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class ComponentBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;
    private final String beanName;
    private final Constructor<?> constructor;

    public ComponentBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.beanName = beanClass.getSimpleName();
        this.constructor = findConstructor(beanClass);
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public Constructor<?> getFactoryMethod() {
        return constructor;
    }

    @Override
    public String getFactoryBeanName() {
        return null;
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.stream(constructor.getParameterTypes()).toList();
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return findAutowiredConstructor(constructors);
    }

    private Constructor<?> findAutowiredConstructor(Constructor<?>[] constructors) {
        Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if (autowiredConstructors.length != 1) {
            throw new RuntimeException("Autowired constructor not found");
        }

        return autowiredConstructors[0];
    }
}
