package com.diy.framework.web.beans.factory;

import java.util.List;

public interface BeanDefinition {
    String getBeanName();

    Class<?> getBeanClass();

    List<Class<?>> getArgumentTypes();

    Object create(DependencyResolver dependencyResolver);
}
