package com.diy.framework.beans.factory;

import com.diy.framework.beans.annotations.Autowired;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
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

    public Constructor<?> scanConstructor(final Class<?> clazz) throws NoSuchMethodException {
        Optional<Constructor<?>> autowired = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> Objects.nonNull(constructor.getDeclaredAnnotation(Autowired.class)))
                .findAny();
        if (autowired.isPresent()) {
            return autowired.get();
        }
        return clazz.getDeclaredConstructor();
    }
}