package com.diy.framework.web.bean.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private static BeanFactory instance;
    private final Map<String, Object> beans;

    private BeanFactory() {
        beans = new ConcurrentHashMap<>();
    }

    public static BeanFactory getBeanFactory() {
        if(instance == null) {
            instance = new BeanFactory();
        }
        return instance;
    }

    public Map<String, Object> getBeans() {
        return beans;
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    public void addBean(Object bean) {
        beans.put(bean.getClass().getSimpleName(), bean);
    }


}
