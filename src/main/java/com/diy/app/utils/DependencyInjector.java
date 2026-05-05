package com.diy.app.utils;

import java.lang.reflect.Field;
import java.util.Set;

public class DependencyInjector {
    private final BeanFactory beanFactory;

    public DependencyInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void inject(Set<Field> fields) {
        for (Field field : fields) {
            try {
                // 주입 대상 (어디에 꽂을 것인가)
                Object target = beanFactory.getBean(field.getDeclaringClass().getName());
                // 주입될 값 (무엇을 꽂을 것인가)
                // DependencyInjector 내부
                Object value = beanFactory.getBean(field.getType().getName());

                if (target != null && value != null) {
                    field.setAccessible(true);
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}