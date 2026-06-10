package com.diy.framework.beans.factory;

import java.lang.reflect.Executable;
import java.util.List;

public interface BeanDefinition {
    Class<?> getBeanClass();
    String getBeanName();
    Executable getFactoryMethod();
    String getFactoryBeanName();
    List<Class<?>> getArgumentTypes();
}
