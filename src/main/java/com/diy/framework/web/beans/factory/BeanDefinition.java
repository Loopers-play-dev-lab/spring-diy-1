package com.diy.framework.web.beans.factory;

import java.lang.reflect.Method;

public record BeanDefinition(
    String name,
    Class<?> beanType,
    Class<?> sourceClass,
    Method factoryMethod
) {

    public static BeanDefinition forComponent(final String name, final Class<?> componentClass) {
        return new BeanDefinition(name, componentClass, componentClass, null);
    }

    public static BeanDefinition forFactoryMethod(
        final String name,
        final Class<?> beanType,
        final Class<?> configurationClass,
        final Method factoryMethod
    ) {
        return new BeanDefinition(name, beanType, configurationClass, factoryMethod);
    }

    public boolean isFactoryMethodBean() {
        return factoryMethod != null;
    }
}
