package com.diy.framework.beans.definition;

import com.diy.framework.context.annotation.Autowired;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class ConstructureBeanDefinition implements BeanDefinition {

    private final Class<?> clazz;
    private final String beanName;
    private final Constructor<?> constructor;

    public ConstructureBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
        this.beanName = clazz.getSimpleName();
        this.constructor = resolveConstructor(clazz);
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public Constructor<?> getFactoryMethod() {
        return constructor;
    }

    @Override
    public String getFactoryBeanName() {
        return null;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    private Constructor<?> resolveConstructor(Class<?> clazz) {
        List<Constructor<?>> constructors = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .toList();

        if (constructors.isEmpty()) {
            return clazz.getDeclaredConstructors()[0];
        }

        if (constructors.size() > 1) {
            throw new RuntimeException("Multiple @Autowired constructors");
        }

        return constructors.getFirst();
    }
}
