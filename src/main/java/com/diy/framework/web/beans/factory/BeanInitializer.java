package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.annotations.Autowired;
import com.diy.framework.web.beans.annotations.Component;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanInitializer {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanInitializer(BeanScanner scanner)
        throws Exception {
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);
        for (Class<?> clazz : classes) {
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : declaredConstructors) {
                if (!constructor.isAnnotationPresent(Autowired.class)) {
                    beans.put(clazz, constructor.newInstance());
                }
            }
        }

        for (Class<?> clazz : classes) {
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : declaredConstructors) {
                if (constructor.isAnnotationPresent(Autowired.class)) {
                    List<Object> list = new ArrayList<>();
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    for (Class<?> parameterType : parameterTypes) {
                        list.add(beans.get(parameterType));
                    }
                    beans.put(clazz, constructor.newInstance(list.toArray()));
                }
            }
        }
    }

    public <T> T getBean(final Class<T> clazz) {
        return (T) beans.get(clazz);
    }
}
