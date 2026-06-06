package com.diy.framework.web.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class AnnotationMetadataUtils {

    private AnnotationMetadataUtils() {
    }

    public static boolean isAnnotatedWith(
        final Class<?> type,
        final Class<? extends Annotation> targetAnnotation
    ) {
        return Arrays.stream(type.getAnnotations())
            .anyMatch(annotation -> isAnnotationMatch(annotation.annotationType(), targetAnnotation, new HashSet<>()));
    }

    private static boolean isAnnotationMatch(
        final Class<? extends Annotation> annotationType,
        final Class<? extends Annotation> targetAnnotation,
        final Set<Class<? extends Annotation>> visitedAnnotations
    ) {
        if (!visitedAnnotations.add(annotationType)) {
            return false;
        }

        if (annotationType.equals(targetAnnotation) || annotationType.isAnnotationPresent(targetAnnotation)) {
            return true;
        }

        return Arrays.stream(annotationType.getAnnotations())
            .map(Annotation::annotationType)
            .anyMatch(annotation -> isAnnotationMatch(annotation, targetAnnotation, visitedAnnotations));
    }
}
