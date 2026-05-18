package com.diy.framework.web.context;

import com.diy.framework.web.beans.factory.BeanDefinition;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.beans.factory.ComponentBeanDefinition;
import com.diy.framework.web.beans.factory.MethodBeanDefinition;
import com.diy.framework.web.context.annotation.Bean;
import com.diy.framework.web.context.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private final List<BeanDefinition> beanDefinitionRegistry = new ArrayList<>();
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(final String... basePackages) {
        BeanScanner scanner = new BeanScanner(basePackages);
        scanner.scanClassesTypeAnnotatedWith(Component.class).forEach(this::registerBean);

        beanDefinitionRegistry.forEach(definition -> {
            if (!isBeanInitialized(definition.getBeanName())) {
                createInstance(definition);
            }
        });
    }

    private void registerBean(Class<?> beanClass) {
        beanDefinitionRegistry.add(new ComponentBeanDefinition(beanClass));

        Arrays.stream(beanClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanDefinitionRegistry.add(
                        new MethodBeanDefinition(method, beanClass.getSimpleName())));
    }

    private Object createInstance(BeanDefinition definition) {
        try {
            definition.getFactoryMethod().setAccessible(true);

            Object[] arguments = resolveBeanArguments(definition.getArgumentTypes());

            if (definition.getFactoryBeanName() == null) {
                Object bean = ((Constructor<?>) definition.getFactoryMethod()).newInstance(arguments);
                saveBean(definition.getBeanName(), bean);
                return bean;
            }

            Object configInstance = getFactoryBean(definition);
            Object bean = ((Method) definition.getFactoryMethod()).invoke(configInstance, arguments);
            saveBean(definition.getBeanName(), bean);
            return bean;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            definition.getFactoryMethod().setAccessible(false);
        }
    }

    private Object[] resolveBeanArguments(List<Class<?>> argumentTypes) {
        return argumentTypes.stream()
                .map(type -> beanDefinitionRegistry.stream()
                        .filter(d -> d.getBeanClass().equals(type))
                        .findFirst()
                        .orElseThrow())
                .map(definition -> {
                    if (isBeanInitialized(definition.getBeanName())) {
                        return getBean(definition.getBeanName());
                    }
                    return createInstance(definition);
                }).toArray();
    }

    private Object getFactoryBean(BeanDefinition definition) {
        if (isBeanInitialized(definition.getFactoryBeanName())) {
            return getBean(definition.getFactoryBeanName());
        }

        BeanDefinition factoryDefinition = beanDefinitionRegistry.stream()
                .filter(d -> d.getBeanName().equals(definition.getFactoryBeanName()))
                .findFirst()
                .orElseThrow();

        return createInstance(factoryDefinition);
    }

    private boolean isBeanInitialized(String beanName) {
        return beans.containsKey(beanName);
    }

    private void saveBean(String beanName, Object bean) {
        beans.put(beanName, bean);
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    public Map<String, Object> getAllBeans() {
        return Collections.unmodifiableMap(beans);
    }
}
