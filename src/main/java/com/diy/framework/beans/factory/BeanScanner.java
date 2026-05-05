package com.diy.framework.beans.factory;

import com.diy.framework.beans.annotations.Autowired;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<Field> scanField(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(field -> Objects.nonNull(field.getDeclaredAnnotation(Autowired.class))).collect(Collectors.toSet());
    }
}