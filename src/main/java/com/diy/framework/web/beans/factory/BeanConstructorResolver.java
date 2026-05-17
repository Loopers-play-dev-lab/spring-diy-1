package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.Autowired;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class BeanConstructorResolver {

    public Constructor<?> resolve(final Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
            .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
            .toArray(Constructor[]::new);

        if (autowiredConstructors.length == 1) {
            return autowiredConstructors[0];
        }

        return Arrays.stream(constructors)
            .filter(constructor -> constructor.getParameterCount() == 0)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("생성자를 결정할 수 없습니다: " + clazz.getName()));
    }
}
