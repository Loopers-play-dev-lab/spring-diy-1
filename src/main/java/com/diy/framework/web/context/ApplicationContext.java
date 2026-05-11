package com.diy.framework.web.context;

import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.context.annotation.Autowired;
import com.diy.framework.web.context.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public ApplicationContext(final String... basePackages) throws Exception {
        BeanScanner scanner = new BeanScanner(basePackages);
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> clazz : classes) {
            createBean(clazz);
        }
    }

    private Object createBean(Class<?> clazz) throws Exception {
        // 이미 생성된 빈이면 그대로 반환
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        // @Autowired 생성자 찾기
        Constructor<?> constructor = findConstructor(clazz);

        // 생성자의 파라미터가 없으면 기본 생성자로 빈 생성
        Class<?>[] paramTypes = constructor.getParameterTypes();
        if (paramTypes.length == 0) {
            Object bean = constructor.newInstance();
            beans.put(clazz, bean);
            return bean;
        }

        // 파라미터가 있으면 각 파라미터에 해당하는 빈을 찾거나 생성
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            params[i] = createBean(paramTypes[i]);
        }

        Object bean = constructor.newInstance(params);
        beans.put(clazz, bean);
        return bean;
    }

    private Constructor<?> findConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        // @Autowired 붙은 생성자 목록 구성
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return constructor;
            }
        }

        // @Autowired 생성자가 1개면 해당 생성자 사용
        if (constructors.length == 1) {
            return constructors[0];
        }

        // @Autowired 생성자 없으면 기본 생성자 사용
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }

        // @Autowired 생성자 여러 개면 에러
        throw new IllegalStateException(
            clazz.getSimpleName() + ": 적합한 생성자를 찾을 수 없습니다."
        );
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }
}
