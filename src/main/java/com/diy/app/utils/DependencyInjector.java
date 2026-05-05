package com.diy.app.utils;

import com.diy.app.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

public class DependencyInjector {
    private final BeanFactory beanFactory;

    public DependencyInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void field_inject(Set<Field> fields) {
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
                    field.setAccessible(false);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    public void constructor_inject(Set<Constructor<?>> constructors) {
        for(Constructor<?> constructor : constructors) {
            try{
                // Autowired가 붙은 생성자인지 확인
                // 주입 대상 (어디에 꽂을 것인가)
                Object target = beanFactory.getBean(constructor.getDeclaringClass().getName());
                // 주입될 값 (무엇을 꽂을 것인가)
                // DependencyInjector 내부
                Object value = beanFactory.getBean(constructor.getParameterTypes()[0].getName());
                if (target != null && value != null) {
                    constructor.setAccessible(true);
                    constructor.newInstance(target,value);
                    constructor.setAccessible(false);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}