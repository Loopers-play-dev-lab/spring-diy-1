package com.diy.framework.web.beans.factory;

import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {
    private final Method method;
    private final Object configInstance;

    public MethodBeanDefinition(Method method, Object configInstance) {
        this.method = method;
        this.configInstance = configInstance;
    }

    @Override
    public Object createInstance(Object[] params) throws Exception {
        return method.invoke(configInstance, params);
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }

    @Override
    public Class<?>[] getDependencyTypes() {
        return method.getParameterTypes();
    }
}
