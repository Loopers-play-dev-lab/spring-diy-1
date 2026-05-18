package com.diy.framework.bean;

import com.diy.framework.bean.annotation.Bean;
import com.diy.framework.bean.annotation.Component;
import com.diy.framework.bean.annotation.Controller;
import com.diy.framework.bean.annotation.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class ApplicationContext {

    private final String basePackage;
    private final Set<Class<?>> beanClasses = new HashSet<>();
    private final Set<Method> beanMethods = new HashSet<>();
    private final List<Object> beans = new ArrayList<>();

    public ApplicationContext(final String basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final BeanScanner beanScanner = new BeanScanner(basePackage);
//        동적 받아온것을 해당 경로의 모든 애너테이션을 등록하기
        beanClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Component.class));
        beanClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Controller.class));
        beanClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Repository.class));
        beanMethods.addAll(beanScanner.scanMethodsAnnotatedWith(Bean.class));

        addComponents();
        addBeans();
    }

    private void addBeans() {
        beanMethods.forEach(method -> {
            Object configBean = getBean(method.getDeclaringClass());
            try {
                Object produce = method.invoke(configBean);
                if (produce != null) {
                    saveBean(produce);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addComponents() {
        beanClasses.forEach(clazz -> {
            if (isBeanInitialized(clazz)) {
                return;
            }

            final Object bean = createInstance(clazz);
            saveBean(bean);
        });
    }

    public <T> T getBean(final Class<T> clazz) {
        return beans.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Bean not found"));
    }

    public List<Object> getBeans() {
        return beans;
    }

    private Object createInstance(final Class<?> clazz) {
        final Constructor<?> constructor = findConstructor(clazz);

        try {
            constructor.setAccessible(true);
            final Object[] parameters = getConstructorParameters(constructor);

            return constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private Constructor<?> findConstructor(final Class<?> clazz) {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return findAutowiredConstructor(constructors);
    }

    private Constructor<?> findAutowiredConstructor(final Constructor<?>[] constructors) {
        final Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if (autowiredConstructors.length != 1) {
            throw new RuntimeException("Autowired constructor not found");
        }

        return autowiredConstructors[0];
    }

    private Object[] getConstructorParameters(final Constructor<?> constructor) {
        final List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes()).toList();

        if (!beanClasses.containsAll(parameterTypes)) {
            throw new RuntimeException("parameter is not bean");
        }

        return parameterTypes.stream().map(parameterType -> {
            if (isBeanInitialized(parameterType)) {
                return getBean(parameterType);
            }

            final Object bean = createInstance(parameterType);
            saveBean(bean);

            return bean;
        }).toArray();
    }

    private boolean isBeanInitialized(Class<?> beanClass) {
        return beans.stream().anyMatch(beanClass::isInstance);
    }

    private void saveBean(Object bean) {
        beans.add(bean);
    }
}
