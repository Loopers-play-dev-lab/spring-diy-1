package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.annotation.Autowired;
import com.diy.framework.web.beans.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private final Map<Class<?>, Object> beans = new HashMap<>();
    private Set<Class<?>> componentClasses;

    public BeanFactory(String... basePackages) throws Exception{
        BeanScanner scanner = new BeanScanner(basePackages);
        componentClasses = scanner.scanClassesTypeAnnotatedWith(Component.class);

        // component 어노테이션이 붙은 클래스들을 불러오고, beans 맵에 없으면 객체를 생성해서 beans 맵에 할당
        for(Class<?> clazz :  componentClasses) {
            if(!beans.containsKey(clazz)){
                Object instance = createBean(clazz);
                beans.put(clazz, instance);
            }
        }
    }


    // 파라미터로 받은 클래스의 객체 생성 메서드
    private Object createBean(Class<?> clazz) throws Exception{
        // 적절한 생성자 찾기
        Constructor<?> constructor = chooseConstructor(clazz);

        // 생성자의 파라미터 타입들 불러오기
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        // 기본 생성자만 있는 경우
        if(parameterTypes.length == 0){
            return constructor.newInstance();
        }

        Object[] parameterInstances = new Object[parameterTypes.length];

        for(int i = 0; i < parameterTypes.length; i++){
            Class<?> parameterType = parameterTypes[i];
            // 해당 파라미터 타입이 인터페이스라면, 구현체를 찾아서 반환해주기
            if(parameterType.isInterface()){
                parameterType = findImplementationClass(parameterType);
            }
            // 생성하려는 객체가 이미 beans 맵에 존재하는지 확인
            if(beans.containsKey(parameterType)){
                parameterInstances[i] = beans.get(parameterType);
            }else{
                // beans 맵에 없다면 createBean을 재귀호출하여 객체 생성
                Object paramInstance = createBean(parameterType);

                // 객체를 재사용하기 위해서 beans 맵에 할당
                beans.put(parameterType, paramInstance);

                parameterInstances[i] = paramInstance;
            }
        }
        return constructor.newInstance(parameterInstances);
    }

    // 알맞은 생성자를 선택하는 메서드
    private Constructor<?> chooseConstructor(Class<?> clazz) throws Exception{
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for(Constructor<?> constructor : constructors){
            // Autowired 어노테이션이 붙은 생성자를 제일 우선으로 반환
            if(constructor.isAnnotationPresent(Autowired.class)){
                return constructor;
            }
        }

        // 생성자가 하나만 있는 경우, 해당 생성자 반환
        if(constructors.length == 1){
            return constructors[0];
        }

        // Autowired도 안 붙어 있고, 생성자가 하나가 아니라면....?
        throw new RuntimeException(clazz.getName() + "어떤 생성자를 선택해야할지 모르겠습니다...");
    }

    // 생성자의 파라미터 타입이 인터페이스라면? 알맞은 구현체를 찾아주는 메서드
    private Class<?> findImplementationClass(Class<?> targetType){
        // 우리가 불러온 component 어노테이션이 붙은 클래스들 중에서 targetType의 구현체가 있는지 찾는다
        for(Class<?> clazz : componentClasses){
            if(targetType.isAssignableFrom(clazz) && !clazz.isInterface()){
                return clazz;
            }
        }
        throw new RuntimeException(targetType.getName() + " 타입의 구현체가 존재하지 않습니다.");
    }

    public <T> T getBean(Class<T> clazz) throws Exception{
        return clazz.cast(beans.get(clazz));
    }
}
