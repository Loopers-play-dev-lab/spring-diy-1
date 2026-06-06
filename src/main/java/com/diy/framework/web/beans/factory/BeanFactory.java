package com.diy.framework.web.beans.factory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final BeanDefinitionRegistry registry;
    private final Map<String, Object> singletonBeans = new HashMap<>();
    private final Set<String> creatingBeans = new HashSet<>();

    public BeanFactory(final BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void preInstantiateSingletons() {
        for (BeanDefinition beanDefinition : registry.getAll()) {
            createBean(beanDefinition.getBeanName());
        }
    }

    public Object getBean(final String name) {
        if (!singletonBeans.containsKey(name)) {
            createBean(name);
        }
        return singletonBeans.get(name);
    }

    public <T> T getBean(final Class<T> type) {
        String beanName = resolveBeanName(type);
        return type.cast(getBean(beanName));
    }

    public <T> Map<String, T> getBeansOfType(final Class<T> type) {
        Map<String, T> beans = new LinkedHashMap<>();
        for (BeanDefinition candidate : registry.getByType(type)) {
            beans.put(candidate.getBeanName(), type.cast(getBean(candidate.getBeanName())));
        }
        return beans;
    }

    public Map<String, Object> getBeansWithAnnotation(final Class<? extends Annotation> annotationType) {
        Map<String, Object> beans = new LinkedHashMap<>();
        for (BeanDefinition candidate : registry.getAll()) {
            if (!AnnotationMetadataUtils.isAnnotatedWith(candidate.getBeanClass(), annotationType)) {
                continue;
            }

            beans.put(candidate.getBeanName(), getBean(candidate.getBeanName()));
        }
        return beans;
    }

    private void createBean(final String beanName) {
        if (singletonBeans.containsKey(beanName)) {
            return;
        }
        if (creatingBeans.contains(beanName)) {
            throw new IllegalStateException("순환 참조가 발생했습니다: " + beanName);
        }

        creatingBeans.add(beanName);
        try {
            BeanDefinition beanDefinition = registry.getByName(beanName);
            Object instance = beanDefinition.create(this::getBean);
            singletonBeans.put(beanName, instance);
        } finally {
            creatingBeans.remove(beanName);
        }
    }

    private String resolveBeanName(final Class<?> type) {
        List<BeanDefinition> candidates = registry.getByType(type);

        if (candidates.isEmpty()) {
            throw new IllegalStateException("등록되지 않은 빈입니다: " + type.getName());
        }

        if (candidates.size() > 1) {
            throw new IllegalStateException("같은 타입의 빈이 여러 개입니다: " + type.getName());
        }

        return candidates.getFirst().getBeanName();
    }
}
