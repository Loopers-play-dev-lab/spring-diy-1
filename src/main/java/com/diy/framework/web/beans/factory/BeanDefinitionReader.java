package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.Bean;
import com.diy.framework.web.annotations.Component;
import com.diy.framework.web.annotations.Configuration;

import java.lang.reflect.Method;
import java.util.Set;

public class BeanDefinitionReader {

    private final BeanScanner beanScanner;
    private final BeanDefinitionRegistry registry;

    public BeanDefinitionReader(final BeanScanner beanScanner, final BeanDefinitionRegistry registry) {
        this.beanScanner = beanScanner;
        this.registry = registry;
    }

    public void loadBeanDefinitions() {
        registerComponentDefinitions(beanScanner.scanClassesTypeAnnotatedWith(Component.class));
        registerConfigurationDefinitions(beanScanner.scanClassesTypeAnnotatedWith(Configuration.class));
    }

    private void registerComponentDefinitions(final Set<Class<?>> componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registry.register(BeanDefinition.forComponent(
                resolveComponentName(componentClass),
                componentClass
            ));
        }
    }

    private void registerConfigurationDefinitions(final Set<Class<?>> configurationClasses) {
        configurationClasses.forEach(this::findBeanMethodsAndRegister);
    }

    private void findBeanMethodsAndRegister(final Class<?> configurationClass) {
        for (Method method : configurationClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Bean.class)) {
                continue;
            }

            Class<?> returnType = method.getReturnType();
            if (returnType == Void.TYPE) {
                throw new IllegalStateException("@Bean 메서드는 값을 반환해야 합니다: " + method.getName());
            }

            registry.register(BeanDefinition.forFactoryMethod(
                resolveBeanMethodName(method),
                returnType,
                configurationClass,
                method
            ));
        }
    }

    private String resolveComponentName(final Class<?> clazz) {
        Component component = clazz.getAnnotation(Component.class);
        if (component == null) {
            throw new IllegalStateException("@Component가 없는 클래스입니다: " + clazz.getName());
        }

        if (!component.value().isBlank()) {
            return component.value();
        }

        return clazz.getSimpleName();
    }

    private String resolveBeanMethodName(final Method method) {
        Bean bean = method.getAnnotation(Bean.class);
        if (bean == null) {
            throw new IllegalStateException("@Bean이 없는 메서드입니다: " + method.getName());
        }

        if (!bean.value().isBlank()) {
            return bean.value();
        }

        return method.getName();
    }
}
