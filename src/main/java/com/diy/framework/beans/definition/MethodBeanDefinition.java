package com.diy.framework.beans.definition;

import com.diy.framework.context.annotation.Bean;
import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {

    private final Class<?> clazz;
    private final String beanName;
    private final Method method;
    private final String factoryBeanName;

    public MethodBeanDefinition(Method method, String factoryBeanName) {
        this.clazz = method.getReturnType();
        this.beanName = resolveBeanName(method);
        this.method = method;
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public Method getFactoryMethod() {
        return method;
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    private String resolveBeanName(Method method) {
        String value = method.getAnnotation(Bean.class).value();

        if (value == null || value.isBlank()) {
            return method.getName();
        }

        return value;
    }
}
