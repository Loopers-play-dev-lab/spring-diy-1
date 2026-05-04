package com.diy.framework.web.beans.factory;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final String... basePackages) {
        reflections = new Reflections(basePackages);
    }

    public List<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) throws NoSuchMethodException {
        LinkedList<Class<?>> collect = reflections.getTypesAnnotatedWith(annotation)
                .stream()
                .filter(type -> (!type.isAnnotation() && !type.isInterface()))
                .distinct()
                .collect(Collectors.toCollection(LinkedList::new));

        List<Class<?>> componentList = new LinkedList<>();

        // 컴포넌트 의존 순서대로 리스트에 담기
        while (!collect.isEmpty() ) {
            Class<?> clazz = collect.pollFirst();
            for (Constructor<?> declaredConstructor : clazz.getDeclaredConstructors()){
                if (declaredConstructor.getParameterCount() == 0) {
                    componentList.addLast(clazz);
                    continue;
                }

                for (Parameter parameter : declaredConstructor.getParameters()) {
                    if (parameter.getClass().isAnnotation() && parameter.getClass().getAnnotation(annotation) != null) {
                        if (!componentList.contains(parameter.getClass())) {
                            collect.addLast(clazz);
                            break;
                        }
                    }
                }
                componentList.addLast(clazz);
            }
        }

        return componentList;
    }
}