package com.diy.framework.bean;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {
    private final Reflections reflections;

    public BeanScanner(final String basePackage) {
        this.reflections = new Reflections(basePackage, Scanners.TypesAnnotated, Scanners.MethodsAnnotated);
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation)
                .stream()
                .filter(type -> (!type.isAnnotation() && !type.isInterface()))
                .collect(Collectors.toSet());
    }

    public Set<Method> scanMethodsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getMethodsAnnotatedWith(annotation);
    }
}
