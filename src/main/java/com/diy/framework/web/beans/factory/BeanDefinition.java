package com.diy.framework.web.beans.factory;

public interface BeanDefinition {
    Object createInstance(Object[] params) throws Exception;

    Class<?> getType();

    Class<?>[] getDependencyTypes();
}
