package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.Bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ConfigurationClassBeanDefinition implements BeanDefinition {
    private static final BeanConstructorResolver CONSTRUCTOR_RESOLVER = new BeanConstructorResolver();

    private final Class<?> beanClass;
    private final String beanName;
    private final Method factoryMethod;
    private final Class<?> configurationClass;


    public ConfigurationClassBeanDefinition(final Class<?> configurationClass, final Method factoryMethod) {
        this.configurationClass = configurationClass;
        this.factoryMethod = factoryMethod;

        if (factoryMethod.getReturnType() == Void.TYPE) {
            throw new IllegalStateException("@Bean 메서드는 값을 반환해야 합니다: " + factoryMethod.getName());
        }

        this.beanClass = factoryMethod.getReturnType();
        final Bean beanAnnotation = factoryMethod.getAnnotation(Bean.class);
        if (beanAnnotation.value() == null || beanAnnotation.value().isBlank()) {
            this.beanName = factoryMethod.getName();
            return;
        }

        this.beanName = beanAnnotation.value();
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
        return Arrays.stream(this.factoryMethod.getParameterTypes()).toList();
    }

    @Override
    public Object create(final DependencyResolver dependencyResolver) {
        try {
            Object configurationInstance = createConfigurationInstance(dependencyResolver);
            factoryMethod.setAccessible(true);
            return factoryMethod.invoke(configurationInstance, resolveArguments(dependencyResolver));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("팩토리 메서드 빈 생성에 실패했습니다: " + beanName, e);
        }
    }

    private Object createConfigurationInstance(final DependencyResolver dependencyResolver) {
        try {
            Constructor<?> constructor = CONSTRUCTOR_RESOLVER.resolve(configurationClass);
            constructor.setAccessible(true);
            Object[] args = Arrays.stream(constructor.getParameterTypes())
                .map(dependencyResolver::resolve)
                .toArray();
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("설정 클래스 생성에 실패했습니다: " + configurationClass.getName(), e);
        }
    }

    private Object[] resolveArguments(final DependencyResolver dependencyResolver) {
        return getArgumentTypes().stream()
            .map(dependencyResolver::resolve)
            .toArray();
    }
}
