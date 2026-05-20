package com.diy.framework.beans.factory;

import java.util.HashSet;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final String... basePackages) {
        reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) {
        final Set<Class<? extends Annotation>> childAnnotations = new HashSet<>();
        collectChildAnnotations(annotation, childAnnotations);

        return childAnnotations.stream()
            .flatMap(child -> reflections.getTypesAnnotatedWith(child).stream())
            .filter(type -> (!type.isAnnotation() && !type.isInterface()))
            .collect(Collectors.toSet());
    }

    private void collectChildAnnotations(
        final Class<? extends Annotation> annotation,
        final Set<Class<? extends Annotation>> visited
    ) {
        if (visited.contains(annotation)) return;

        visited.add(annotation);
        reflections.getTypesAnnotatedWith(annotation).stream()
            .filter(Class::isAnnotation)
            .forEach(type -> collectChildAnnotations(type.asSubclass(Annotation.class), visited));
    }
}
