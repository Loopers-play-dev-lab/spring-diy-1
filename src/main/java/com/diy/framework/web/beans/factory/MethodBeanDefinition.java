package com.diy.framework.web.beans.factory;

import com.diy.framework.web.context.annotation.Bean;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MethodBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;
    private final String beanName;
    private final Method factoryMethod;
    private final String factoryBeanName;

    public MethodBeanDefinition(Method factoryMethod, String factoryBeanName) {
        this.factoryMethod = factoryMethod;
        this.factoryBeanName = factoryBeanName;
        this.beanClass = factoryMethod.getReturnType();

        Bean beanAnnotation = factoryMethod.getAnnotation(Bean.class);
        if (beanAnnotation.name() == null || beanAnnotation.name().isBlank()) {
            this.beanName = factoryMethod.getName();
        } else {
            this.beanName = beanAnnotation.name();
        }
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
    public Method getFactoryMethod() {
        return factoryMethod;
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.stream(factoryMethod.getParameterTypes()).toList();
    }
}
