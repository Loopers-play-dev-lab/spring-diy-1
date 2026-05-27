package com.diy.framework.beans.factory;

import com.diy.framework.context.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.List;

public class AnnotatedGenericBeanDefinition implements BeanDefinition{
    private Class<?> beanClass;
    private String beanName;
    private Constructor<?> constructor;

    public AnnotatedGenericBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.beanName = beanClass.getSimpleName();
        this.constructor = findConstructor(beanClass);
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public Executable getFactoryMethod() {
        return this.constructor;
    }

    @Override
    public List<Class<?>> getArgumentsType() {
        return Arrays.stream(this.constructor.getParameterTypes()).toList();
    }

    @Override
    public String getFactoryBeanName() {
        return null;
    }

    public Constructor<?> findConstructor(Class<?> beanClass) {
        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();

        if(constructors.length == 1) {
            return constructors[0];
        }

        return findAutowiredConstructor(constructors);
    }

    private Constructor<?> findAutowiredConstructor(Constructor<?>[] constructors) {
        Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if(autowiredConstructors.length == 0) {
            throw new RuntimeException("Autowired 생성자가 없습니다.");
        }

        if(autowiredConstructors.length > 1) {
            throw new RuntimeException("Autowired 생성자는 하나여야합니다.");
        }

        return autowiredConstructors[0];
    }
}
