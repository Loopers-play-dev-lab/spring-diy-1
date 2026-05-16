package com.diy.framework.web.beans.factory;

import com.diy.framework.web.context.annotation.Autowired;

import java.lang.reflect.Constructor;

public class ComponentBeanDefinition implements BeanDefinition {
    private final Class<?> clazz;
    private final Constructor<?> constructor;

    public ComponentBeanDefinition(Class<?> clazz) throws NoSuchMethodException {
        this.clazz = clazz;
        this.constructor = findConstructor(clazz);
    }

    @Override
    public Object createInstance(Object[] params) throws Exception {
        return constructor.newInstance(params);
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }

    @Override
    public Class<?>[] getDependencyTypes() {
        return constructor.getParameterTypes();
    }

    private Constructor<?> findConstructor(Class<?> clazz) throws NoSuchMethodException {
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

        throw new IllegalStateException(
            clazz.getSimpleName() + ": 적합한 생성자를 찾을 수 없습니다."
        );
    }
}
