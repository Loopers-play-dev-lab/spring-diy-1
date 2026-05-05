package com.diy.framework.web.beans.factory;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public void addBean(final Class<?> clazz, final Object bean) {
        // 생성한 빈 저장
        beans.put(clazz, bean);
    }

    public Object getBean(final Class<?> clazz) {
        // 저장된 빈 조회
        return beans.get(clazz);
    }
}