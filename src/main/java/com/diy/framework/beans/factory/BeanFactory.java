package com.diy.framework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface BeanFactory {

    Object getBean(String name);

    <T> T getBean(Class<T> requiredType);

    <T> Map<String, T> getBeansOfType(Class<T> type);

    <T> String[] getBeanNamesForType(Class<T> type);

    String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);

    <A extends Annotation> A findAnnotationOnBean(Object bean, Class<A> annotationType);
}
