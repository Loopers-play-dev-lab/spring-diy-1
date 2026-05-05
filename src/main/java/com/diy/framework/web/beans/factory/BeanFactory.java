package com.diy.framework.web.beans.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public void addBean(final Class<?> clazz, final Object bean) {
        // 생성한 빈 저장
        beans.put(clazz, bean);
    }

    public void initialize(final Set<Class<?>> beanClasses) throws Exception {
        // 빈 클래스들로 객체 생성 후 저장
        for (Class<?> beanClass : beanClasses) {
            Object bean = beanClass.getDeclaredConstructor().newInstance();
            addBean(beanClass, bean);
        }
    }

    public Object getBean(final Class<?> clazz) {
        // 저장된 빈 조회
        return beans.get(clazz);
    }
}