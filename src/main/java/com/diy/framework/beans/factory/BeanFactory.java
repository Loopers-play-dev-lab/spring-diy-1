package com.diy.framework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface BeanFactory {
    Object getBean(final String name);
    <T> T getBean(final Class<T> requiredType);
    <T> Map<String, T> getBeansOfType(final Class<T> type);
    <T> String[] getBeanNamesForType(final Class<T> type);
    String[] getBeanNamesForAnnotation(final Class<? extends Annotation> annotationType);
    <A extends Annotation> A findAnnotationOnBean(final Object bean, final Class<A> annotationType);
}
