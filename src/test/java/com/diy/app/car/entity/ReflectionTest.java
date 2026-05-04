package com.diy.app.car.entity;

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

}