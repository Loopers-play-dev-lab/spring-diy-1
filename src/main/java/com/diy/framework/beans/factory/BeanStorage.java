package com.diy.framework.beans.factory;

import com.diy.framework.beans.annotations.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanStorage {

    private static BeanStorage instance;

    private final Map<String, Object> beans = new HashMap<>();
    private final BeanScanner bc = new BeanScanner("com.diy.app");

    private BeanStorage() {
        Set<Class<?>> classes = bc.scanClassesTypeAnnotatedWith(Component.class);
        makeBeans(classes);
    }

    public static BeanStorage getInstance() {
        if (Objects.isNull(instance)) instance = new BeanStorage();
        return instance;
    }

    private void makeBeans(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            makeBean(clazz);
        }
    }

    private Object makeBean(Class<?> clazz) {
        Constructor<?> constructor = null;
        try {
            constructor = bc.scanConstructor(clazz);

            Object[] params = Arrays.stream(constructor.getParameterTypes())
                    .map(paramType -> findBean(paramType).orElseGet(() -> makeBean(paramType)))
                    .toArray();

            constructor.setAccessible(true);
            Object bean = constructor.newInstance(params);
            beans.put(clazz.getName(), bean);
            return bean;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        } finally {
            if (constructor != null) {
                constructor.setAccessible(false);
            }
        }
    }

    private Optional<Object> findBean(final Class<?> classType) {
        return beans.values().stream()
                .filter(classType::isInstance)
                .findAny();
    }

    public <T> Optional<T> getBean(final Class<T> classType) {
        return beans.values().stream()
                .filter(classType::isInstance)
                .map(classType::cast)
                .findAny();
    }
}
