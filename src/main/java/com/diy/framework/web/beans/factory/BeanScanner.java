package com.diy.framework.web.beans.factory;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {
    private final Reflections reflections;

    public BeanScanner(final String... basePackages) {
        reflections = new Reflections((Object[]) basePackages);
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) {
        Set<Class<?>> annotatedTypes = new HashSet<>(reflections.getTypesAnnotatedWith(annotation));
        for (Class<? extends Annotation> stereotype : findStereotypes(annotation, new HashSet<>())) {
            annotatedTypes.addAll(reflections.getTypesAnnotatedWith(stereotype));
        }

        return annotatedTypes.stream()
            .filter(type -> !type.isAnnotation() && !type.isInterface())
            .collect(Collectors.toSet());
    }

    private Set<Class<? extends Annotation>> findStereotypes(
        final Class<? extends Annotation> annotationType,
        final Set<Class<? extends Annotation>> visitedAnnotations
    ) {
        if (!visitedAnnotations.add(annotationType)) {
            return Set.of();
        }

        Set<Class<? extends Annotation>> stereotypes = reflections.getTypesAnnotatedWith(annotationType).stream()
            .filter(Class::isAnnotation)
            .map(this::toAnnotationType)
            .collect(Collectors.toSet());

        Set<Class<? extends Annotation>> nestedStereotypes = stereotypes.stream()
            .flatMap(stereotype -> findStereotypes(stereotype, visitedAnnotations).stream())
            .collect(Collectors.toSet());

        stereotypes.addAll(nestedStereotypes);
        return stereotypes;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Annotation> toAnnotationType(final Class<?> type) {
        return (Class<? extends Annotation>) type;
    }
}
