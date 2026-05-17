package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnnotatedGenericBeanDefinition implements BeanDefinition {
    private static final BeanConstructorResolver CONSTRUCTOR_RESOLVER = new BeanConstructorResolver();

    private final Class<?> beanClass;
    private final String beanName;
    private final Constructor<?> constructor;

    public AnnotatedGenericBeanDefinition(final Class<?> beanClass) {
        this.beanClass = beanClass;
        this.beanName = resolveComponentName(beanClass);
        this.constructor = CONSTRUCTOR_RESOLVER.resolve(beanClass);
    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.stream(this.constructor.getParameterTypes()).toList();
    }

    @Override
    public Object create(final DependencyResolver dependencyResolver) {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(resolveArguments(dependencyResolver));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("컴포넌트 빈 생성에 실패했습니다: " + beanName, e);
        }
    }

    private Object[] resolveArguments(final DependencyResolver dependencyResolver) {
        return getArgumentTypes().stream()
            .map(dependencyResolver::resolve)
            .toArray();
    }

    private String resolveComponentName(final Class<?> clazz) {
        String componentName = Arrays.stream(clazz.getAnnotations())
            .map(annotation -> resolveComponentName(annotation, new HashSet<>()))
            .filter(name -> !name.isBlank())
            .findFirst()
            .orElse("");

        if (!componentName.isBlank()) {
            return componentName;
        }

        if (!hasComponentAnnotation(clazz)) {
            throw new IllegalStateException("@Component가 없는 클래스입니다: " + clazz.getName());
        }

        return clazz.getSimpleName();
    }

    private boolean hasComponentAnnotation(final Class<?> clazz) {
        return Arrays.stream(clazz.getAnnotations())
            .anyMatch(annotation -> isComponentAnnotation(annotation.annotationType(), new HashSet<>()));
    }

    private boolean isComponentAnnotation(
        final Class<? extends Annotation> annotationType,
        final Set<Class<? extends Annotation>> visitedAnnotations
    ) {
        if (!visitedAnnotations.add(annotationType)) {
            return false;
        }

        if (annotationType.equals(Component.class) || annotationType.isAnnotationPresent(Component.class)) {
            return true;
        }

        return Arrays.stream(annotationType.getAnnotations())
            .map(Annotation::annotationType)
            .anyMatch(annotation -> isComponentAnnotation(annotation, visitedAnnotations));
    }

    private String resolveComponentName(
        final Annotation annotation,
        final Set<Class<? extends Annotation>> visitedAnnotations
    ) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        if (!visitedAnnotations.add(annotationType)) {
            return "";
        }

        boolean directComponent = annotationType.equals(Component.class);
        boolean metaComponent = annotationType.isAnnotationPresent(Component.class);
        if (!directComponent && !metaComponent) {
            String metaComponentName = Arrays.stream(annotationType.getAnnotations())
                .map(metaAnnotation -> resolveComponentName(metaAnnotation, new HashSet<>(visitedAnnotations)))
                .filter(name -> !name.isBlank())
                .findFirst()
                .orElse("");
            if (!metaComponentName.isBlank()) {
                return metaComponentName;
            }

            return "";
        }

        try {
            Method valueMethod = annotationType.getMethod("value");
            Object value = valueMethod.invoke(annotation);
            if (value instanceof String stringValue && !stringValue.isBlank()) {
                return stringValue;
            }
        } catch (NoSuchMethodException e) {
            return "";
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("컴포넌트 이름을 읽을 수 없습니다: " + annotationType.getName(), e);
        }

        return Arrays.stream(annotationType.getAnnotations())
            .map(metaAnnotation -> resolveComponentName(metaAnnotation, new HashSet<>(visitedAnnotations)))
            .filter(name -> !name.isBlank())
            .findFirst()
            .orElse("");
    }
}
