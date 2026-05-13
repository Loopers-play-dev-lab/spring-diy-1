package com.diy.framework.web.beans.factory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Map<String, Object> beansByName = new HashMap<>();

    public BeanFactory(final String... basePackages) {
        BeanScanner scanner = new BeanScanner(basePackages);
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class); //프레임워크 컴파일 시점 클래스 객체 수집-> 클래스 객체 @component 붙어있는 애들 찾기
        for (Class<?> clazz : classes) {
            createBean(clazz); //빈으로 만듬
        }
    }

    public <T> T getBean(final Class<T> type) {
        return type.cast(beans.get(type));
    }

    public Object getBean(String name) {return beansByName.get(name); }

    public Map<Class<?>, Object> getBeans() {
        return Collections.unmodifiableMap(beans);
    }

    private Object createBean(final Class<?> clazz) {
        if (beans.containsKey(clazz)) { //메모리에 잇나?
            return beans.get(clazz);
        }

        try {
            Constructor<?> constructor = findConstructor(clazz);
            Object[] params = Arrays.stream(constructor.getParameterTypes())
                    .map(this::createBean)
                    .toArray();
            Object instance = constructor.newInstance(params);
            beans.put(clazz, instance); //기존 (클래스타입, 오브젝트)

            String simpleName = clazz.getSimpleName(); //클래스명 가져오기
            String beanName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1); //앞글자만 소문자
            beansByName.put(beanName, instance);

            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> findConstructor(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElseGet(() -> {
                    try {
                        return clazz.getDeclaredConstructor();
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
