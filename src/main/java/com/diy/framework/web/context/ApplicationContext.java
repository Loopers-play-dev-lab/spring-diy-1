package com.diy.framework.web.context;

import com.diy.framework.web.anotation.Autowired;
import com.diy.framework.web.anotation.Component;
import com.diy.framework.web.beans.factory.BeanScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public ApplicationContext(final String... basePackages) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        BeanScanner scanner = new BeanScanner(basePackages);
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> clazz : classes) {
            createBean(clazz);
        }
    }

    private void createBean(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (beans.containsKey(clazz)) {
            return;
        }

        Constructor<?> constructor = findConstructor(clazz);
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        if (parameterTypes.length == 0) {
            Object bean = constructor.newInstance();
            beans.put(clazz, bean);
            return;
        }

        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = beans.get(parameterTypes[i]);
        }

        Object bean = constructor.newInstance(params);
        beans.put(clazz, bean);
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return constructor;
            }
        }

        if (constructors.length == 1) {
            return constructors[0];
        }

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }

        throw new IllegalStateException();
    }
}
