package com.diy.framework.web.beans.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

public class BeanContainer {

    private static final Map<Class<?>, Object> beanMap = new HashMap<>();

    public static void register(Set<Class<?>> clazzSet) {
        for (Class<?> aClass : clazzSet) {
            register(aClass);
        }
    }

    public static void register(Class<?> clazz) {
        Object o = create(clazz, beanMap.values().toArray());
        beanMap.put(clazz, o);
    }

    public static void inject(Set<Constructor> constructors) {
        for (Constructor constructor : constructors) {
            inject(constructor);
        }
    }

    public static void inject(Constructor constructor) {
        constructor.setAccessible(true);
        Parameter[] parameters = constructor.getParameters();
        for (Parameter parameter : parameters) {
            Object o = beanMap.get(parameter.getType());
        }
        Class declaringClass = constructor.getDeclaringClass();
    }

    private static Object create(Class<?> clazz, Object... args) {
        try {
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            Optional<Object> o = Optional.empty();
            for (Constructor<?> declaredConstructor : declaredConstructors) {
                try {
                    boolean isAllBeanStored = new HashSet<>(Arrays.stream(args)
                            .map(Object::getClass)
                            .toList())
                            .containsAll(
                                    Arrays.stream(declaredConstructor.getParameters()).map(Parameter::getType).toList()
                            );
                    if (!isAllBeanStored) { throw new IllegalArgumentException(); }
                    o = Optional.of(declaredConstructor.newInstance(args));
                    System.out.println("Bean 생성 => " + o);
                    break;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                catch (IllegalArgumentException ignored) {}
            }
            return o.orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Bean 객체를 생성할 수 없습니다.");
        }
    }

}
