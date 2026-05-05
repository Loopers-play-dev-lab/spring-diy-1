package com.diy.framework.web.beans.factory;

import java.util.HashMap;
import java.util.Map;

public class BeanContainer {

    private static final Map<Class<?>, Object> map = new HashMap<>();

    public void register(Class<?> clazz, Object o) {
        map.put(clazz, o);
    }

    public Object inject(Class<?> clazz) {
        return map.get(clazz);
    }

}
