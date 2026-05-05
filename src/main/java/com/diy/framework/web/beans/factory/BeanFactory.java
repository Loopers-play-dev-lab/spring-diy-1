package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final Map<Class<?>, Object> beans = new HashMap<>();
    private Set<Class<?>> beanClasses;

    public void addBean(final Class<?> clazz, final Object bean) {
        // 생성한 빈 저장
        beans.put(clazz, bean);
    }

    public void initialize(final Set<Class<?>> beanClasses) throws Exception {
        this.beanClasses = beanClasses;

        // 빈 클래스들로 객체 생성 후 저장
        for (Class<?> beanClass : beanClasses) {
            if (getBean(beanClass) == null) {
                Object bean = createBean(beanClass);
                addBean(beanClass, bean);
            }
        }
    }

    public Object getBean(final Class<?> clazz) {
        // 저장된 빈 조회
        return beans.get(clazz);
    }

    private Object createBean(final Class<?> beanClass) throws Exception {
        // 사용할 생성자 찾기
        Constructor<?> constructor = findConstructor(beanClass);

        // 생성자 파라미터 타입 가져오기
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        // 파라미터 없는 생성자면 바로 객체 생성
        if (parameterTypes.length == 0) {
            return constructor.newInstance();
        }

        // 생성자 파라미터에 넣을 빈들 찾기
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = getOrCreateBean(parameterTypes[i]);
        }

        // 찾은 빈들을 생성자에 넣어서 객체 생성
        return constructor.newInstance(parameters);
    }

    private Object getOrCreateBean(final Class<?> beanClass) throws Exception {
        Object bean = getBean(beanClass);
        if (bean != null) {
            return bean;
        }

        if (!beanClasses.contains(beanClass)) {
            throw new IllegalArgumentException("빈 클래스를 찾을 수 없습니다: " + beanClass.getName());
        }

        bean = createBean(beanClass);
        addBean(beanClass, bean);
        return bean;
    }

    private Constructor<?> findConstructor(final Class<?> beanClass) throws NoSuchMethodException {
        // Autowired 붙은 생성자 찾기
        for (Constructor<?> constructor : beanClass.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return constructor;
            }
        }

        // 없으면 기본 생성자 사용
        return beanClass.getDeclaredConstructor();
    }
}