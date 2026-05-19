package com.diy.framework.web.beans.factory;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final String... basePackages) {
        reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> doComponentScan(final Class<? extends Annotation> annotation) throws NoSuchMethodException {
        Set<Class<?>> classes = scanClassesTypeAnnotatedWith(annotation);

        List<Class<? extends Annotation>> componentChildAnnotations = reflections.getTypesAnnotatedWith(annotation)
                .stream()
                .filter(type -> (type.isAnnotation() && type.isInterface()))
                .map(type -> (Class<? extends Annotation>) type)
                .collect(Collectors.toList());
        for (Class<? extends Annotation> componentChildAnnotation : componentChildAnnotations) {
            classes.addAll(scanClassesTypeAnnotatedWith(componentChildAnnotation));
        }

        return classes;
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) throws NoSuchMethodException {
        return reflections.getTypesAnnotatedWith(annotation)
                .stream()
                .filter(type -> (!type.isAnnotation() && !type.isInterface()))
                .collect(Collectors.toSet());
    }
}