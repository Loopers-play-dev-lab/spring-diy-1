package com.diy.app.utils;

import com.diy.app.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.*;

public class BeanFactory {
    private final Map<String, Object> singletonObjects = new HashMap<>();
    private final Map<Class<?>, List<Object>> beanNamesByType = new HashMap<>();

    // 빈 네이밍 규칙
    private String determineBeanName(Class<?> clazz) {
        String shortName = clazz.getSimpleName();
        return java.beans.Introspector.decapitalize(shortName);
    }
    public void createBean(Set<Class<?>> componentClasses, Set<Constructor<?>> constructors, DependencyInjector injector){
        for (Class<?> clazz : componentClasses) {
            // @Autowired가 없는 클래스 먼저 생성
            boolean hasAutowired = Arrays.stream(clazz.getDeclaredConstructors())
                    .anyMatch(constructor -> constructor.isAnnotationPresent(Autowired.class));
            if (!hasAutowired) {
                createDefaultInstance(clazz);
            }
        }
        for (Constructor<?> constructor : constructors) {
            String beanName = determineBeanName(constructor.getDeclaringClass());
            // 1. 빈 등록 여부 확인
            if(singletonObjects.containsKey(beanName)) continue;

            // 2. 실제 인스턴스 생성
            Object instance = injector.createInstanceByConsturctor(constructor);

            registerBean(beanName, instance);
        }
    }
    private void createDefaultInstance(Class<?> clazz) {
        try {
            Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            Object instance = defaultConstructor.newInstance();
            registerBean(determineBeanName(clazz), instance);
            defaultConstructor.setAccessible(false);
        } catch (Exception e) {
            throw new IllegalStateException(clazz.getName() + " 빈 생성 중 오류 발생: " + e.getMessage(), e);
        }
    }
    public void registerBean(String beanName, Object instance) {
        // 중복 생성 방지 (인터페이스 혹은 상속의 경우)
        if(singletonObjects.containsKey(beanName)){
            throw new IllegalStateException("이미 존재하는 빈 이름: " + beanName);
        }
        singletonObjects.put(beanName, instance);

        Class<?> current = instance.getClass();
        // Object class 전까지 부모 클래스 탐색
        while (current != null && !current.equals(Object.class)) {
            addTypeIndex(current,instance);

            for(Class<?> iface : current.getInterfaces()){
                addTypeIndex(iface,instance);
            }
            current = current.getSuperclass();
        }
    }
    private void addTypeIndex(Class<?> type , Object instance) {
        beanNamesByType.computeIfAbsent(type, k -> new ArrayList<>()).add(instance);
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
    public Map<Class<?>, List<Object>> getTypeIndexMap() {
        return Collections.unmodifiableMap(beanNamesByType); // 읽기 전용 Map
    }

    public Object getBeanByType(Class<?> type) {
        List<Object> beans = beanNamesByType.get(type);
        if(beans == null || beans.isEmpty()) return null;

        if(beans.size() > 1) {
            throw new IllegalStateException(
                    type.getSimpleName() + "타입의 빈이" + beans.size() + "개 스캔되어 어떤 빈을 주입할지 결정할 수 없습니다"
            );
        }
        return beans.get(0);
    }
}
