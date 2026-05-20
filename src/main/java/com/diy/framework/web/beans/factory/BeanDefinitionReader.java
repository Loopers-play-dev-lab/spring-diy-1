package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.Bean;
import com.diy.framework.web.annotations.Component;
import com.diy.framework.web.annotations.Configuration;
import com.diy.framework.web.annotations.Controller;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class BeanDefinitionReader {

    private final BeanScanner beanScanner;
    private final BeanDefinitionRegistry registry;

    public BeanDefinitionReader(final BeanScanner beanScanner, final BeanDefinitionRegistry registry) {
        this.beanScanner = beanScanner;
        this.registry = registry;
    }

    public void loadBeanDefinitions() {
        Set<Class<?>> componentClasses = new HashSet<>(beanScanner.scanClassesTypeAnnotatedWith(Component.class));
        componentClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Controller.class));

        registerComponentDefinitions(componentClasses);
        registerConfigurationDefinitions(beanScanner.scanClassesTypeAnnotatedWith(Configuration.class));
    }

    private void registerComponentDefinitions(final Set<Class<?>> componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registry.register(new AnnotatedGenericBeanDefinition(componentClass));
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

            registry.register(new ConfigurationClassBeanDefinition(configurationClass, method));
        }
    }
}
