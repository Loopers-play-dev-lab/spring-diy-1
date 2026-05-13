package com.diy.framework.beans.factory;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ClassBeanDefinition implements BeanDefinition {

    private final String name;
    private final Class<?> clazz;

    public ClassBeanDefinition(final String name, final Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }

    @Override
    public Object create(final ApplicationContext context) {
        final Constructor<?> constructor = findConstructor();
        try {
            constructor.setAccessible(true);
            final Object[] parameters = Arrays.stream(constructor.getParameterTypes())
                    .map(context::getBean)
                    .toArray();
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private Constructor<?> findConstructor() {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return Arrays.stream(constructors)
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Autowired constructor not found: " + clazz.getName()));
    }
}
