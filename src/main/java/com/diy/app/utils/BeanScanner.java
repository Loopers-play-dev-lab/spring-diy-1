package com.diy.app.utils;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private final Reflections reflections;

    public BeanScanner(final String... basePackages) {
        this.reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(basePackages)
                .addScanners(
                        Scanners.TypesAnnotated,  // @Component 스캔용
                        Scanners.FieldsAnnotated, // @Autowired 스캔용
                        Scanners.SubTypes         // 상속/인터페이스 스캔용
                ));
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation)
                .stream()
                .filter(type -> (!type.isAnnotation() && !type.isInterface()))
                .collect(Collectors.toSet());
    }
    public Set<Field> scanFieldTypeAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getFieldsAnnotatedWith(annotation);
    }
}
