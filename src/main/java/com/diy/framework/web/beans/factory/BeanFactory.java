package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.factory.annotation.Autowired;
import com.diy.framework.web.beans.factory.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {

    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Set<Class<?>> componentClasses;

    public BeanFactory(final String... basePackages) {
        final BeanScanner scanner = new BeanScanner(basePackages);
        this.componentClasses = scanner.scanClassesTypeAnnotatedWith(Component.class);

        for (final Class<?> clazz : componentClasses) {
            getOrCreateBean(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        final Object bean = beans.get(clazz);
        if (bean == null) {
            throw new IllegalStateException("등록되지 않은 빈입니다: " + clazz.getName());
        }
        return (T) bean;
    }

    private Object getOrCreateBean(final Class<?> requestedType) {
        if (beans.containsKey(requestedType)) {
            return beans.get(requestedType);
        }

        final Class<?> concreteClass = resolveConcreteClass(requestedType);

        if (beans.containsKey(concreteClass)) {
            final Object existing = beans.get(concreteClass);
            beans.put(requestedType, existing);
            return existing;
        }

        final Constructor<?> constructor = findConstructor(concreteClass);
        final Object[] args = Arrays.stream(constructor.getParameterTypes())
                .map(this::getOrCreateBean)
                .toArray();

        try {
            final Object bean = constructor.newInstance(args);
            beans.put(concreteClass, bean);
            if (!concreteClass.equals(requestedType)) {
                beans.put(requestedType, bean);
            }
            return bean;
        } catch (final ReflectiveOperationException e) {
            throw new IllegalStateException("빈 생성 실패: " + concreteClass.getName(), e);
        }
    }

    private Class<?> resolveConcreteClass(final Class<?> type) {
        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers())) {
            if (!componentClasses.contains(type)) {
                throw new IllegalStateException("@Component 가 아닌 의존성은 주입할 수 없습니다: " + type.getName());
            }
            return type;
        }

        final List<Class<?>> candidates = componentClasses.stream()
                .filter(type::isAssignableFrom)
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            throw new IllegalStateException(type.getName() + " 의 구현체 빈을 찾을 수 없습니다.");
        }
        if (candidates.size() > 1) {
            throw new IllegalStateException(
                    type.getName() + " 의 구현체 빈이 여러 개입니다: " + candidates);
        }
        return candidates.get(0);
    }

    private Constructor<?> findConstructor(final Class<?> clazz) {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        for (final Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                constructor.setAccessible(true);
                return constructor;
            }
        }

        for (final Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                constructor.setAccessible(true);
                return constructor;
            }
        }

        throw new IllegalStateException("@Autowired 또는 기본 생성자가 필요합니다: " + clazz.getName());
    }
}
