package com.diy.framework.context;

import com.diy.framework.beans.factory.AnnotatedGenericBeanDefinition;
import com.diy.framework.beans.factory.BeanDefinition;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.beans.factory.ConfigurationClassBeanDefinition;
import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.context.annotation.RequestMapping;
import com.diy.framework.web.mvc.ControllerV1;
import com.diy.framework.web.mvc.controller.AnnotatedControllerDefinition;
import com.diy.framework.web.mvc.controller.ControllerDefinition;
import com.diy.framework.web.mvc.controller.InterfaceControllerDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
        beanScanner.scanClassesTypeAnnotatedWith(Controller.class).forEach(this::registerBean);

        beanDefinitionRegistry.forEach(beanDefinition -> {
            final String beanName = beanDefinition.getBeanName();

            if (isBeanInitialized(beanName)) {
                return;
            }

            createInstance(beanDefinition);
        });
    }

    public List<ControllerDefinition> getControllerBeans() {
        return beanDefinitionRegistry.stream().map(beanDefinition -> {
            System.out.println(beanDefinition.getBeanName());
            if(ControllerV1.class.isAssignableFrom(beanDefinition.getBeanClass())) {
                Object bean = beans.get(beanDefinition.getBeanName());
                return new InterfaceControllerDefinition(beanDefinition.getBeanName(), (ControllerV1) bean);
            }

            if(beanDefinition.getBeanClass().isAnnotationPresent(Controller.class)) {
                Object bean = beans.get(beanDefinition.getBeanName());
                String path = beanDefinition.getBeanClass().getAnnotation(RequestMapping.class).value();
                return new AnnotatedControllerDefinition(path, bean);
            }

            return null;
        }).filter(Objects::nonNull).toList();
    }

    private void registerBean(final Class<?> beanClass) {
        System.out.println(beanClass.getName());
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
