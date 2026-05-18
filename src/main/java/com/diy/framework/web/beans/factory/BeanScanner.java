package com.diy.framework.web.beans.factory;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final String... basePackages) {
        reflections = new Reflections(basePackages, new SubTypesScanner(false));
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .filter(type -> (!type.isAnnotation() && !type.isInterface()))
                .filter(type -> hasAnnotation(type, annotation))
                .collect(Collectors.toSet());
    }

    private boolean hasAnnotation(Class<?> type, Class<? extends Annotation> target) {
        return Arrays.stream(type.getAnnotations())
                .anyMatch(a -> isOrContains(a.annotationType(), target));
    }

    private boolean isOrContains(Class<? extends Annotation> current, Class<? extends Annotation> target) {
        if (current == target) {
            return true;
        }
        return Arrays.stream(current.getAnnotations())
                .map(Annotation::annotationType)
                .filter(a -> !a.getPackageName().startsWith("java.lang"))
                .anyMatch(a -> isOrContains(a, target));
    }
}
