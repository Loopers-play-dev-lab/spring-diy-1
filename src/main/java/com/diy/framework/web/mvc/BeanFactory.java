package com.diy.framework.web.mvc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanFactory {
    private final Map<String, Object> beanContainer = new HashMap<>();
    private final Set<String> created = new HashSet<>();

    public void initialize(String basePackage){
        BeanScanner beanScanner = new BeanScanner(basePackage);
        beanScanner.scanClassesTypeAnnotatedWith(Component.class)
                .forEach(this::getOrCreate);
    }

    public void printBeanContainer(){
        this.beanContainer.keySet().forEach(System.out::println);
    }

    public Object getOrCreate(Class<?> clazz){
        if (beanContainer.containsKey(clazz.getName())) {
            return beanContainer.get(clazz.getName());
        }

        if (!created.add(clazz.getName())) {
            throw new RuntimeException("순환 참조가 발생했습니다. " + clazz.getName());
        }

        try {
            // 적절한 생성자 찾기
            Constructor<?> constructor = findConstructor(clazz);

            // 인스턴스 생성
            Object bean = null;
            if (constructor.getParameterCount() == 0){
                bean = constructor.newInstance();
            } else {
                bean = resolveParams(constructor);
            }
            beanContainer.put(constructor.getName(), bean);
            created.remove(clazz.getName());
            return bean;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Constructor<?> findConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?> targetConstructor = null;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if(constructor.isAnnotationPresent(Autowired.class)){
                targetConstructor = constructor;
                break;
            }
        }
        if (targetConstructor == null) targetConstructor = clazz.getDeclaredConstructor();
        return targetConstructor;
    }

    public Object[] resolveParams(Constructor<?> constructor){
        return Arrays.stream(constructor.getParameterTypes())
                .map(this::getOrCreate)
                .toArray();
    }
}
