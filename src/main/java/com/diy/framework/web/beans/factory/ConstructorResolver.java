package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.Autowired;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConstructorResolver {

    public Constructor<?> resolve(final Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (hasSingleConstructor(constructors)) {
            return constructors[0];
        }

        Constructor<?> singleAutowiredConstructor = getSingleAutowiredConstructor(getAutowiredConstructors(constructors), clazz);
        return Objects.requireNonNullElseGet(singleAutowiredConstructor, () -> Arrays.stream(constructors)
            .filter(this::isNoArgConstructor)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("생성자를 결정할 수 없습니다: " + clazz.getName())));
    }

    private boolean hasSingleConstructor(final Constructor<?>[] constructors) {
        return constructors.length == 1;
    }

    private List<Constructor<?>> getAutowiredConstructors(final Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
            .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
            .toList();
    }

    private Constructor<?> getSingleAutowiredConstructor(final List<Constructor<?>> autowiredConstructors, final Class<?> clazz) {
        if (autowiredConstructors.size() > 1) {
            throw new IllegalStateException("@Autowired 생성자가 여러 개입니다: " + clazz.getName());
        }

        if (autowiredConstructors.size() == 1) {
            return autowiredConstructors.getFirst();
        }

        return null;
    }

    private boolean isNoArgConstructor(final Constructor<?> constructor) {
        return constructor.getParameterCount() == 0;
    }
}
