package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflection1Test {
    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println(carClass.getName());
    }

    @Test
    @DisplayName("Car 필드 가져오기")
    void showField() {
        Class<Car> carClass = Car.class;
        for(Field field : carClass.getDeclaredFields()) {
            System.out.println(field.getName());
        }
    }

    @Test
    @DisplayName("Car 생성자 가져오기")
    void showConstructor() {
        Class<Car> carClass = Car.class;
        Constructor<?>[] constructors = carClass.getDeclaredConstructors();
        for(Constructor constructor : carClass.getDeclaredConstructors()) {
            int filedCount = constructor.getParameterTypes().length;
            System.out.print(constructor.getName() + "(");
            for(int i = 0; i < filedCount; i++) {
                System.out.print(constructor.getParameters()[i].getType());
                if(i != filedCount - 1) System.out.print(", ");
            }
            System.out.println(")");
        }
    }

    @Test
    @DisplayName("Car 메서드 가져오기")
    void showAllMethod() {
        Class<Car> carClass = Car.class;
        for(Method method : carClass.getMethods()) {
            int filedCount = method.getParameterTypes().length;
            System.out.print(method.getName() + "(");
            for(int i = 0; i < filedCount; i++) {
                System.out.print(method.getParameters()[i].getType());
                if(i != filedCount - 1) System.out.print(", ");
            }
            System.out.println(")");
        }
    }
}
