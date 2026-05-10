package com.diy.app.utils;

import com.diy.app.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.*;

public class BeanFactory {
    private final Map<String, Object> singletonObjects = new HashMap<>();
    private final Map<Class<?>, List<Object>> beanNamesByType = new HashMap<>();

    public void createBean(Set<Class<?>> componentClasses){
        for (Class<?> clazz : componentClasses) {
            // @Autowired가 없는 클래스 먼저 생성
            boolean constructorAutowired = Arrays.stream(clazz.getDeclaredConstructors()).anyMatch(constructor -> constructor.isAnnotationPresent(Autowired.class));
            if (!constructorAutowired) {
                try {
                    Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
                    defaultConstructor.setAccessible(true);
                    Object instance = defaultConstructor.newInstance();
                    registerBean(clazz, instance);
                    defaultConstructor.setAccessible(false);
                } catch (Exception e) {
                    throw new IllegalStateException(clazz.getName() + " 빈 생성 중 오류 발생: " + e.getMessage(), e);
                }
            }
        }
    }
    public void registerBean(Class<?> clazz, Object instance) {
        String beanName = clazz.getName();
        // 중복 생성 방지 (인터페이스 혹은 상속의 경우)
        if(!singletonObjects.containsKey(beanName)){
            singletonObjects.put(beanName, instance);
        }
        Class<?> current = clazz;
        // Object class 전까지 부모 클래스 탐색
        while (current != null && !current.equals(Object.class)) {
            addTypeIndex(current,beanName);

            for(Class<?> iface : current.getInterfaces()){
                addTypeIndex(iface,beanName);
            }
            current = current.getSuperclass();
        }
    }
    private void addTypeIndex(Class<?> type , String beanName) {
        beanNamesByType.computeIfAbsent(type, k -> new ArrayList<>()).add(beanName);
    }
    public Object getBean(String name) {
        return singletonObjects.get(name);
    }

    public Map<String, Object> getSingletonObjects() {
        return Collections.unmodifiableMap(singletonObjects); // 읽기 전용 Map
    }

    /**
     * [모니터링용] 현재 팩토리에 등록된 모든 빈의 타입별 매핑 정보를 반환
     * 특정 인터페이스나 클래스 타입으로 어떤 빈들이 조회 가능한지 확인할 수 있습니다.
     *
      * @return Map<Class<?>, List<Object>>
     */
    public Map<Class<?>, List<Object>> getBeanNamesByType() {
        return Collections.unmodifiableMap(beanNamesByType); // 읽기 전용 Map
    }
}
