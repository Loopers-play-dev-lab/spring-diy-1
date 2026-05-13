package com.diy.framework.web.beans.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Map<String, Object> beansByName = new HashMap<>();

    public BeanFactory(final String... basePackages) throws InvocationTargetException, IllegalAccessException {
        BeanScanner scanner = new BeanScanner(basePackages);
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class); //프레임워크 컴파일 시점 클래스 객체 수집-> 클래스 객체 @component 붙어있는 애들 찾기
        for (Class<?> clazz : classes) {
            Object instance = createBean(clazz); //빈으로 만듬

            //@Bean 메소드가 있는 클래스 찾기
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods){
                createBeanInstance(method,instance);
            }
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

    //빈 어노테이션이 붙은 메소드 찾기
    private void createBeanInstance(Method method, Object instance)
            throws InvocationTargetException, IllegalAccessException {

        if (method.isAnnotationPresent(Bean.class)) {
            Object beanInstance = method.invoke(instance);

            beans.put(beanInstance.getClass(), beanInstance);

            Bean annotation = method.getAnnotation(Bean.class);
            String beanName = annotation.value().isEmpty() ? method.getName() : annotation.value();
            beansByName.put(beanName, beanInstance);
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
