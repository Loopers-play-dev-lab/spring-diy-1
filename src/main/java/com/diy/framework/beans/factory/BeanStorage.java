package com.diy.framework.beans.factory;

import com.diy.framework.beans.annotations.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public class BeanStorage {

    private final List<?> beans;

    public BeanStorage() {
        BeanScanner bc = new BeanScanner("com.diy.app");
        Set<Class<?>> classes = bc.scanClassesTypeAnnotatedWith(Component.class);
        beans = classes.stream().map(clazz -> {
            Constructor<?> constructor = null;
            try {
                constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            } finally {
                if (constructor != null) {
                    constructor.setAccessible(false);
                }
            }
        }).toList();
    }

    public <T> List<T> getBeans(final Class<T> classType) {
        return beans.stream()
                .filter(classType::isInstance)
                .map(classType::cast)
                .toList();
    }
}
