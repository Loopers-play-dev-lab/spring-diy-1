package com.diy.framework.beans.factory;

import com.diy.framework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodBeanDefinition implements BeanDefinition {

    private final String name;
    private final Method method;

    public MethodBeanDefinition(final String name, final Method method) {
        this.name = name;
        this.method = method;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }

    @Override
    public Object create(final ApplicationContext context) {
        final Object factoryBean = context.getBean(method.getDeclaringClass());
        try {
            method.setAccessible(true);
            final Object[] parameters = Arrays.stream(method.getParameterTypes())
                    .map(context::getBean)
                    .toArray();
            return method.invoke(factoryBean, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            method.setAccessible(false);
        }
    }
}
