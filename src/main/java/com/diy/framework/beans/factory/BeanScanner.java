package com.diy.framework.beans.factory;

import com.diy.framework.context.annotation.Bean;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.beans.Introspector.decapitalize;

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

    /**
     * @Bean 어노테이션의 문자열 value 값으로 빈 이름-인스턴스 매핑
     * */
    public Map<String, Class<?>> scanBeansTypeAnnotatedWithName() {
        Map<String, Class<?>> container = new HashMap<>();
        Set<Class<?>> beanClasses = scanClassesTypeAnnotatedWith(Bean.class);

        for(Class<?> clazz: beanClasses) {
            try {
                Bean bean = clazz.getAnnotation(Bean.class);
                // Bean 어노테이션에 등록된 value값 추출
                String beanName = bean.value();

                //  빈 이름이 없으면 클래스 기본명을 소문자화하여 저장
                if(beanName.isBlank()) {
                    beanName = decapitalize(clazz.getSimpleName());
                }

                // 중복 빈 이름 예외처리하기
                if(container.containsKey(beanName)){
                    throw new IllegalArgumentException(beanName + "빈이 이미 등록되어 있습니다!");
                }

                container.put(beanName, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return container;
    }
}
