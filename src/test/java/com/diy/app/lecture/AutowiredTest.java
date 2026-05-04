package com.diy.app.lecture;

import com.diy.framework.web.beans.factory.Autowired;
import com.diy.framework.web.beans.factory.Component;
import com.diy.framework.web.beans.factory.BeanScanner;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AutowiredTest {

    @Test
    @DisplayName("빈 주입 확인")
    void 빈주입확인() throws Exception {
        BeanScanner scanner = new BeanScanner("com.diy");
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);

        Map<Class<?>, Object> beans = new HashMap<>();

        for (Class<?> clazz : classes) {
            createBean(clazz, beans);
        }

        beans.forEach((type, instance) ->
                System.out.println(type.getSimpleName() + " → " + instance));
    }

    private Object createBean(Class<?> clazz, Map<Class<?>, Object> beans) throws Exception {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);  //이미 만들어진 빈이면 재사용
        }


        //@Autowired 생성자 찾기
        Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElseGet(() -> {
                    try {
                        return clazz.getDeclaredConstructor();
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });  //없으면 기본 생성자

        //파라미터 타입별로 빈 먼저 생성
        Object[] params = Arrays.stream(constructor.getParameterTypes())
                .map(paramType -> {
                    try {
                        return createBean(paramType, beans);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray();

        Object instance = constructor.newInstance(params);
        beans.put(clazz, instance);
        return instance;
    }

}
