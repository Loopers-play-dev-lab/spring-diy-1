package com.diy.framework.web.beans.factory;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final String... basePackages) {
        // 스캔할 패키지 설정
        reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) {
        // 특정 애너테이션이 붙은 클래스 찾기
        return reflections.getTypesAnnotatedWith(annotation)
                .stream()
                .filter(type -> (!type.isAnnotation() && !type.isInterface()))
                .collect(Collectors.toSet());
    }
}