package com.diy.app.utils;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory {
    private final Map<String, Object> beans = new HashMap<>();

    public void registerBean(String name, Object instance) {
        beans.put(name, instance);
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    public Map<String, Object> getAllBeans() {
        return beans;
    }
}
