package com.diy.framework.beans.definition;

import java.lang.reflect.Executable;

public interface BeanDefinition {

    Class<?> getBeanClass();

    String getBeanName();

    Executable getFactoryMethod();

    String getFactoryBeanName();

    Class<?>[] getParameterTypes();
}
