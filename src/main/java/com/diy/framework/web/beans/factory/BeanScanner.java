package com.diy.framework.web.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final String... basePackages) {
        reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation)
            .stream()
            .filter(type -> (!type.isAnnotation() && !type.isInterface()))
            .collect(Collectors.toSet());
    }
}
