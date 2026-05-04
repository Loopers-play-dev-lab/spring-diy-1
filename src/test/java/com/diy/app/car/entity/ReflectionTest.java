package com.diy.app.car.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReflectionTest {

    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println(carClass.getName());
    }

    @Test
    @DisplayName("Car의 모든 메소드 목록을 구한다")
    void showMethodAll(){
        Class<Car> carClass = Car.class;
        System.out.println(Arrays.toString(carClass.getDeclaredMethods()));
    }

    @Test
    @DisplayName("메소드 이름이 test로 시작하는 경우")
    void starWithTest() throws Exception {
        Class<Car> carClass = Car.class;
        Object instance =   carClass.getDeclaredConstructor(String.class, int.class).newInstance("소나타", 3000);                                                        ;

        for (Method method : carClass.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                System.out.println("메소드명: " + method.getName());
                System.out.println(method.invoke(instance));
            }
        }
    }


    @Test
    @DisplayName("@PrintView 애노테이션 메소드 실행")
    void testAnnotationMethodRun() throws Exception{
        Class<Car> carClass = Car.class;
        Object instance =   carClass.getDeclaredConstructor(String.class, int.class).newInstance("소나타", 3000);

        for (Method method : carClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PrintView.class)) {
                System.out.println("메소드명: " + method.getName());
                method.invoke(instance);
            }
        }
    }

    @Test
    @DisplayName("private field에 값 할당")
    void privateFieldAccess() throws Exception {
        Class<Car> carClass = Car.class;
        Object instance = carClass.getDeclaredConstructor(String.class, int.class).newInstance("소나타", 3000);

        Field[] allField = carClass.getDeclaredFields();

        for (Field field : allField) {
            field.setAccessible(true);
            if (field.getName().equals("name")) {
                field.set(instance, "벤츠");
            } else if (field.getName().equals("price")) {
                field.set(instance, 1000);
            }
        }

        for (Method method : carClass.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                System.out.println("메소드명: " + method.getName());
                System.out.println(method.invoke(instance));
            }
        }
    }



}