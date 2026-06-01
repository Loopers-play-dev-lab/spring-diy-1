package com.diy.framework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BeanFactoryUtils {
    public static <T> Map<String, T> beansOfTypeIncludingAncestors(final BeanFactory beanFactory, final Class<T> type) {
        return beanFactory.getBeansOfType(type);
    }

    public static Map<String, Object> beansOfAnnotated(final BeanFactory beanFactory, final Class<? extends Annotation> annotation) {
        final LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        final String[] beanNames = beanFactory.getBeanNamesForAnnotation(annotation);

        Arrays.stream(beanNames).forEach(beanName -> result.put(beanName, beanFactory.getBean(beanName)));

        return result;
    }
}
