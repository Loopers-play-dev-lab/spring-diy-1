package com.diy.framework.beans.factory;

import com.diy.framework.context.annotation.Bean;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ConfigurationClassBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass; //
    private final String beanName;
    private final Method factoryMethod;
    private final String factoryBeanName;

    public ConfigurationClassBeanDefinition(Method factoryMethod, String factoryBeanName) {
        this.factoryMethod = factoryMethod;
        this.factoryBeanName = factoryBeanName;

        this.beanClass = factoryMethod.getReturnType();
        Bean beanAnnotation = factoryMethod.getAnnotation(Bean.class);
        if(beanAnnotation.value() == null || beanAnnotation.value().isBlank()) {
            this.beanName = factoryMethod.getName();
            return;
        }

        this.beanName = beanAnnotation.value();
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
    public Method getFactoryMethod() {
        return this.factoryMethod;
    }

    @Override
    public List<Class<?>> getArgumentsType() {
        return Arrays.stream(this.factoryMethod.getParameterTypes()).toList();
    }

    @Override
    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }
}
