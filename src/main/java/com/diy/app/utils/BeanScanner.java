package com.diy.app.utils;

import com.diy.app.annotation.Component;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private Map<String,Object> bean = new HashMap();

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

    public void createBean () {
        Set<Class<?>> classes = scanClassesTypeAnnotatedWith(Component.class); // Lecture 조회됨
        try {
            for (Class<?> aClass : classes) {
                if(bean.get(aClass.getName()) == null) {
                    return;
                }
                bean.put(aClass.getName(), aClass.getDeclaredConstructor().newInstance()); // 인스턴스 생성
                System.out.println(aClass.getName()+aClass.getAnnotation(Component.class).value()+" 생성!");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
