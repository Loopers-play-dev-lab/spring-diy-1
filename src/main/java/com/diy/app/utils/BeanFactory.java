package com.diy.app.utils;

import com.diy.app.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private final Map<String, Object> beans = new HashMap<>();

    public void createBean(Set<Class<?>> componentClasses){
        for (Class<?> clazz : componentClasses) {
            // @Autowired가 없는 클래스 먼저 생성
            boolean constructorAutowired = Arrays.stream(clazz.getDeclaredConstructors()).anyMatch(constructor -> constructor.isAnnotationPresent(Autowired.class));
            if (!constructorAutowired) {
                try {
                    Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
                    defaultConstructor.setAccessible(true);
                    Object instance = defaultConstructor.newInstance();
                    registerBean(clazz.getName(), instance);
                    for (Class<?> iface : clazz.getInterfaces()) {
                        // 인터페이스 이름으로 등록 (com.diy.app.repo.ILectureRepository)
                        registerBean(iface.getName(), instance);
                    }
                    defaultConstructor.setAccessible(false);
                } catch (Exception e) {
                    throw new IllegalStateException(clazz.getName() + " 빈 생성 중 오류 발생: " + e.getMessage(), e);
                }
            }
        }
    }
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
