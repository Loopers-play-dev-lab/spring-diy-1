package com.diy.app.utils;

import com.diy.app.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class DependencyInjector {
    private final BeanFactory beanFactory;

    public DependencyInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    // 필드 주입 방법
    public void fieldInject(Set<Field> fields) {
        for (Field field : fields) {
            try {
                // 주입 대상 (어디에 꽂을 것인가)
                Object target = beanFactory.getBean(field.getDeclaringClass().getName());
                // 주입될 값 (무엇을 꽂을 것인가)
                Object value = beanFactory.getBean(field.getType().getName());

                if (target != null && value != null) {
                    field.setAccessible(true);
                    field.set(target, value);
                    field.setAccessible(false);
                }
            } catch (Exception e) {
                throw new IllegalStateException(field.getDeclaringClass() + field.getName() + "필드 주입 중 오류 발생: " + e.getMessage(), e);
            }
        }
    }

    // 생성자 주입 방법
    public void constructorInject(Set<Constructor<?>> constructors) {
        for (Constructor<?> constructor : constructors) {
            try {
                Class<?> clazz = constructor.getDeclaringClass();

                // 생성자 파라미터 타입 조회
                Object[] args = Arrays.stream(constructor.getParameterTypes()).map(type -> beanFactory.getBean(type.getName())).toArray();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance(args);
                beanFactory.registerBean(clazz.getName(), instance);
                for (Class<?> iface : clazz.getInterfaces()) {
                    beanFactory.registerBean(iface.getName(), instance);
                }
                constructor.setAccessible(false);
            } catch (Exception e) {
                throw new IllegalStateException(constructor.getName() + "생성자 주입 중 오류 발생" + e.getMessage(), e);
            }
        }
    }
}