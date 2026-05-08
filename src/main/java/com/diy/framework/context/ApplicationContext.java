package com.diy.framework.context;

import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ApplicationContext {

    private final String basePackage;
    private final Set<Class<?>> beanClasses = new HashSet<>();
    private final List<Object> beans = new ArrayList<>();

    public ApplicationContext(String basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        BeanScanner beanScanner = new BeanScanner(this.basePackage);
        beanClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Component.class));

        for(Class<?> clazz : beanClasses) {
            if(isBeanInitialized(clazz)) {
                return;
            }

            saveBean(createInstance(clazz));
        }
    }

    private Object createInstance(Class<?> clazz) {
        Constructor<?> constructor = findConstructor(clazz);

        try{
            constructor.setAccessible(true);

            final Object[] parameters = getConstructorParameters(constructor);

            return constructor.newInstance(parameters);

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }   finally {
            constructor.setAccessible(false);
        }
    }

    private Object[] getConstructorParameters(Constructor<?> constructor) {
        List<Class<?>> parameterTypes =  Arrays.stream(constructor.getParameterTypes()).toList();

        if(!beanClasses.containsAll(parameterTypes)) {
            throw new RuntimeException("파라미터 타입이 bean이 아닙니다.");
        }

        return parameterTypes.stream().map(parameterType -> {
            if(isBeanInitialized(parameterType)) {
                return beans.stream().filter(bean -> bean.getClass() == parameterType).findFirst().orElseThrow();
            }

            Object bean = createInstance(parameterType);
            saveBean(bean);

            return bean;
        }).toArray();
    }

    private void saveBean(Object bean) {
        beans.add(bean);
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if(constructors.length == 1) {
            return constructors[0];
        }

        return findAutowiredConstructor(constructors);
    }

    private Constructor<?> findAutowiredConstructor(Constructor<?>[] constructors) {
        Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if(autowiredConstructors.length == 0) {
            throw new RuntimeException("Autowired 생성자가 없습니다.`");
        }

        if(autowiredConstructors.length > 1) {
            throw new RuntimeException("Autowired 생성자는 하나여야합니다.");
        }

        return autowiredConstructors[0];
    }

    private boolean isBeanInitialized(Class<?> clazz) {
        return beans.stream().anyMatch(bean -> bean.getClass() == clazz);
    }

    public Object getBean(Class<?> clazz) {
        return beans.stream().filter(bean -> bean.getClass() == clazz).findFirst().orElseThrow();
    }
}
