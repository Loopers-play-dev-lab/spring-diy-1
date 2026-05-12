package com.diy.framework.web.beans.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final BeanDefinitionRegistry registry;
    private final ConstructorResolver constructorResolver;
    private final Map<String, Object> singletonBeans = new HashMap<>();
    private final Set<String> creatingBeans = new HashSet<>();

    public BeanFactory(final BeanDefinitionRegistry registry, final ConstructorResolver constructorResolver) {
        this.registry = registry;
        this.constructorResolver = constructorResolver;
    }

    public void preInstantiateSingletons() {
        for (BeanDefinition beanDefinition : registry.getAll()) {
            createBean(beanDefinition.name());
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
            Object instance = beanDefinition.isFactoryMethodBean()
                ? createByFactoryMethod(beanDefinition)
                : createByConstructor(beanDefinition);
            singletonBeans.put(beanName, instance);
        } finally {
            creatingBeans.remove(beanName);
        }
    }

    private Object createByConstructor(final BeanDefinition beanDefinition) {
        try {
            Constructor<?> constructor = constructorResolver.resolve(beanDefinition.sourceClass());
            constructor.setAccessible(true);
            return constructor.newInstance(resolveArguments(constructor.getParameterTypes()));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createByFactoryMethod(final BeanDefinition beanDefinition) {
        Method factoryMethod = beanDefinition.factoryMethod();
        if (factoryMethod == null) {
            throw new IllegalStateException("팩토리 메서드가 없는 빈입니다: " + beanDefinition.name());
        }

        try {
            Object configurationInstance = createConfigurationInstance(beanDefinition.sourceClass());
            factoryMethod.setAccessible(true);
            return factoryMethod.invoke(configurationInstance, resolveArguments(factoryMethod.getParameterTypes()));
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createConfigurationInstance(final Class<?> configurationClass) {
        try {
            Constructor<?> constructor = constructorResolver.resolve(configurationClass);
            constructor.setAccessible(true);
            return constructor.newInstance(resolveArguments(constructor.getParameterTypes()));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] resolveArguments(final Class<?>[] parameterTypes) {
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = getBean(parameterTypes[i]);
        }
        return args;
    }

    private String resolveBeanName(final Class<?> type) {
        List<BeanDefinition> candidates = registry.getByType(type);

        if (candidates.isEmpty()) {
            throw new IllegalStateException("등록되지 않은 빈입니다: " + type.getName());
        }

        if (candidates.size() > 1) {
            throw new IllegalStateException("같은 타입의 빈이 여러 개입니다: " + type.getName());
        }

        return candidates.getFirst().name();
    }
}
