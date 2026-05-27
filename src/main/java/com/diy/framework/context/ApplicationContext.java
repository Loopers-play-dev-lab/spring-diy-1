package com.diy.framework.context;

import com.diy.framework.beans.factory.AnnotatedGenericBeanDefinition;
import com.diy.framework.beans.factory.BeanDefinition;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.beans.factory.ConfigurationClassBeanDefinition;
import com.diy.framework.context.annotation.*;
import com.diy.framework.web.mvc.controller.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ApplicationContext {

    private final String basePackage;
    private final List<BeanDefinition> beanDefinitionRegistry = new ArrayList<>();
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(String basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        BeanScanner beanScanner = new BeanScanner(this.basePackage);
        beanScanner.scanClassesTypeAnnotatedWith(Component.class).forEach(this::registerBean);

        beanDefinitionRegistry.forEach(beanDefinition -> {
            String beanName = beanDefinition.getBeanName();

            if(isBeanInitialized(beanName)) {
                return;
            }

            createInstance(beanDefinition);
        });
    }

    private void registerBean(Class<?> beanClass) {
        this.beanDefinitionRegistry.add(new AnnotatedGenericBeanDefinition(beanClass));
        postProcessBeanDefinitionRegistry(beanClass);
    }

    private void postProcessBeanDefinitionRegistry(Class<?> beanClass) {
        Arrays.stream(beanClass.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanDefinitionRegistry.add(new ConfigurationClassBeanDefinition(method, beanClass.getSimpleName())));
    }

    private Object createInstance(BeanDefinition beanDefinition) {
        Executable factoryMethod = beanDefinition.getFactoryMethod();

        try{
            factoryMethod.setAccessible(true);

            Object[] arguments = resolveBeanArguments(beanDefinition.getArgumentsType());

            if(beanDefinition.getFactoryBeanName() == null) {
                Object bean = autowireConstructor((Constructor<?>) factoryMethod, arguments);
                saveBean(beanDefinition.getBeanName(), bean);

                return bean;
            }

            Object bean = instantiateUsingFactoryMethod(beanDefinition, arguments);
            saveBean(beanDefinition.getBeanName(), bean);

            return bean;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }   finally {
            factoryMethod.setAccessible(false);
        }
    }

    private Object[] resolveBeanArguments(List<Class<?>> argumentsType) {
        return argumentsType.stream()
                .map(argumentType -> beanDefinitionRegistry.stream()
                        .filter(definition -> definition.getBeanClass().equals(argumentType))
                        .findFirst()
                        .get())
                .map(beanDefinition -> {
                    String beanName = beanDefinition.getBeanName();
                    if(isBeanInitialized(beanName)) {
                        return getBean(beanName);
                    }

                    return createInstance(beanDefinition);
                }).toArray();
    }

    private Object autowireConstructor(Constructor<?> constructor, Object[] arguments) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return constructor.newInstance(arguments);
    }

    private Object instantiateUsingFactoryMethod(BeanDefinition beanDefinition, Object[] arguments) throws InvocationTargetException, IllegalAccessException {
        if(!(beanDefinition instanceof ConfigurationClassBeanDefinition)) {
            throw new RuntimeException("required ConfigurationClassBeanDefinition.");
        }

        Method method = (Method) beanDefinition.getFactoryMethod();
        return method.invoke(getFactoryBean(beanDefinition), arguments);
    }

    private Object getFactoryBean(BeanDefinition beanDefinition) {
        String factoryBeanName = beanDefinition.getFactoryBeanName();

        if(isBeanInitialized(factoryBeanName)) {
            return getBean(factoryBeanName);
        }

        BeanDefinition factoryBeanDefinition = beanDefinitionRegistry.stream()
                .filter(definition -> definition.getBeanName().equals(factoryBeanName))
                .findFirst().get();

        return createInstance(factoryBeanDefinition);
    }

    private void saveBean(String name, Object bean) {
        if(beans.containsKey(name)) {
            throw new RuntimeException("동일한 이름의 빈이 이미 존재합니다.");
        }

        beans.put(name, bean);
    }

    private boolean isBeanInitialized(String beanName) {
        return beans.containsKey(beanName);
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    public Map<String, Controller> getControllersMapping() {
        Map<String, Controller> controllersMapping = new HashMap<>();

        beans.values().stream()
                .filter(bean -> bean.getClass().isAnnotationPresent(RequestMapping.class)).toList()
                .forEach(bean -> controllersMapping.put(bean.getClass().getDeclaredAnnotation(RequestMapping.class).value(), (Controller) bean));

        return controllersMapping;
    }
}
