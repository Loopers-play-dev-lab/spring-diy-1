package com.diy.framework.web.beans.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BeanContainer {

    private static final Map<Class<?>, Object> map = new HashMap<>();

    public static void register(Class<?> clazz) {
        Object o = create(clazz);
        map.put(clazz, o);
    }

    public static Object inject(Class<?> clazz) {
        return map.get(clazz);
    }

    private static <T> T create(Class<?> clazz) {
        try {
            return (T) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Bean 객체를 생성할 수 없습니다. 이유: " + e.getMessage());
        }
    }

}
