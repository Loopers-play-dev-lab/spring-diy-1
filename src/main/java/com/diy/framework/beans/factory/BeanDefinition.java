package com.diy.framework.beans.factory;

import com.diy.framework.context.ApplicationContext;

public interface BeanDefinition {

    String getName();

    Class<?> getType();

    Object create(ApplicationContext context);
}
