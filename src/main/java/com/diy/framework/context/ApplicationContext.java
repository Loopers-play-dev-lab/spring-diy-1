package com.diy.framework.context;

import com.diy.framework.beans.factory.AnnotatedGenericBeanDefinition;
import com.diy.framework.beans.factory.BeanDefinition;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.beans.factory.ConfigurationClassBeanDefinition;
import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private final String basePackage;
    private final List<BeanDefinition> beanDefinitionRegistry = new ArrayList<>();
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(final String basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final BeanScanner beanScanner = new BeanScanner(basePackage);
        beanScanner.scanClassesTypeAnnotatedWith(Component.class).forEach(this::registerBean);

        beanDefinitionRegistry.forEach(beanDefinition -> {
            final String beanName = beanDefinition.getBeanName();

            if (isBeanInitialized(beanName)) {
                return;
            }

            createInstance(beanDefinition);
        });
    }

    private void registerBean(final Class<?> beanClass) {
        this.beanDefinitionRegistry.add(new AnnotatedGenericBeanDefinition(beanClass));
        postProcessBeanDefinitionRegistry(beanClass);
    }

    private void postProcessBeanDefinitionRegistry(final Class<?> beanClass) {
        Arrays.stream(beanClass.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanDefinitionRegistry.add(new ConfigurationClassBeanDefinition(method, beanClass.getSimpleName())));
    }

    private Object createInstance(final BeanDefinition beanDefinition) {
        final Executable factoryMethod = beanDefinition.getFactoryMethod();

        try {
            factoryMethod.setAccessible(true);

            final Object[] arguments = resolveBeanArguments(beanDefinition.getArgumentTypes());

            if (beanDefinition.getFactoryBeanName() == null) {
                final Object bean = autowireConstructor((Constructor<?>) factoryMethod, arguments);
                saveBean(beanDefinition.getBeanName(), bean);

                return bean;
            }

            final Object bean = instantiateUsingFactoryMethod(beanDefinition, arguments);
            saveBean(beanDefinition.getBeanName(), bean);

            return bean;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            factoryMethod.setAccessible(false);
        }
    }

    private Object[] resolveBeanArguments(final List<Class<?>> argumentsType) {
        return argumentsType.stream()
                .map(argumentType -> beanDefinitionRegistry.stream()
                        .filter(definition -> definition.getBeanClass().equals(argumentType))
                        .findFirst()
                        .get())
                .map(beanDefinition -> {
                    if (isBeanInitialized(beanDefinition.getBeanName())) {
                        return getBean(beanDefinition.getBeanName());
                    }

                    return createInstance(beanDefinition);
                }).toArray();
    }

    private Object instantiateUsingFactoryMethod(final BeanDefinition beanDefinition, final Object[] arguments) throws InvocationTargetException, IllegalAccessException {
        if (!(beanDefinition instanceof ConfigurationClassBeanDefinition)) {
            throw new RuntimeException("required ConfigurationClassBeanDefinition.");
        }

        return ((Method) beanDefinition.getFactoryMethod()).invoke(getFactoryBean(beanDefinition), arguments);
    }

    private Object getFactoryBean(final BeanDefinition beanDefinition) {
        if (isBeanInitialized(beanDefinition.getFactoryBeanName())) {
            return getBean(beanDefinition.getFactoryBeanName());
        }

        final BeanDefinition factoryBeanDefinition = beanDefinitionRegistry.stream()
                .filter(definition -> definition.getBeanName().equals(beanDefinition.getFactoryBeanName()))
                .findFirst().get();

        return createInstance(factoryBeanDefinition);
    }

    private Object autowireConstructor(final Constructor<?> constructor, final Object[] arguments) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return constructor.newInstance(arguments);
    }

    private boolean isBeanInitialized(final String beanName) {
        return beans.containsKey(beanName);
    }

    private void saveBean(final String beanName, final Object bean) {
        beans.put(beanName, bean);
    }

    private Object getBean(final String beanName) {
        return beans.get(beanName);
    }
}
