package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.Autowired;
import com.diy.framework.web.annotations.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Set<Class<?>> creatingBeans = new HashSet<>();
    private final Set<Class<?>> beanClasses;

    public BeanContainer(String basePackages) {
        this.beanClasses = new BeanScanner(basePackages)
            .scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> clazz : this.beanClasses) {
            createBean(clazz);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beans.get(clazz));
    }

    private void createBean(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return;
        }
        if (creatingBeans.contains(clazz)) {
            throw new IllegalStateException("순환 참조가 발생했습니다: " + clazz.getName());
        }
        creatingBeans.add(clazz);

        try {
            Constructor<?> constructor = findConstructor(clazz);
            constructor.setAccessible(true);

            Object instance;
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            if (parameterTypes.length == 0) {
                instance = constructor.newInstance();
            } else {
                Object[] args = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> dependencyType = resolveDependencyType(parameterTypes[i]);
                    createBean(dependencyType);
                    args[i] = beans.get(dependencyType);
                }
                instance = constructor.newInstance(args);
            }

            beans.put(clazz, instance);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            creatingBeans.remove(clazz);
        }
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        List<Constructor<?>> autowiredConstructors = Arrays.stream(constructors)
            .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
            .toList();

        if (autowiredConstructors.size() > 1) {
            throw new IllegalStateException("@Autowired 생성자가 여러 개입니다: " + clazz.getName());
        }

        if (autowiredConstructors.size() == 1) {
            return autowiredConstructors.getFirst();
        }

        return Arrays.stream(constructors)
            .filter(constructor -> constructor.getParameterCount() == 0)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("생성자를 결정할 수 없습니다: " + clazz.getName()));
    }

    private Class<?> resolveDependencyType(Class<?> type) {
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            List<Class<?>> candidates = beanClasses.stream()
                .filter(type::isAssignableFrom)
                .toList();

            if (candidates.isEmpty()) {
                throw new IllegalStateException("구현체가 없습니다: " + type.getName());
            }

            if (candidates.size() > 1) {
                throw new IllegalStateException("구현체가 여러 개입니다: " + type.getName());
            }

            return candidates.getFirst();
        }

        if (!type.isAnnotationPresent(Component.class)) {
            throw new IllegalStateException("등록되지 않은 빈입니다: " + type.getName());
        }

        return type;
    }
}
