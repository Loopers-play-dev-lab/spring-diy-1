package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {

    @DisplayName("요구사항 1 - 클래스 정보 출력")
    @Test
    void showClass() {
        Class<Car> carClass = Car.class;

        // 클래스 이름
        System.out.println("Name: " + carClass.getName());

        // 모든 필드
        for (Field field : carClass.getDeclaredFields()) {
            System.out.println("Field: " + field.getName() + " / Type: " + field.getType());
        }

        // 모든 생성자
        for (Constructor<?> constructor : carClass.getDeclaredConstructors()) {
            System.out.println("Constructor: " + constructor);
        }

        // 모든 메서드
        for (Method method : carClass.getDeclaredMethods()) {
            System.out.println("Method: " + method.getName());
        }
    }

    @DisplayName("요구사항 2 - test로 시작하는 메서드 실행")
    @Test
    void testMethodRun() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                Object result = method.invoke(car);
                System.out.println(result);
            }
        }
    }

    @DisplayName("요구사항 3 - @PrintView 애너테이션 메서드 실행")
    @Test
    void testAnnotationMethodRun() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PrintView.class)) {
                method.invoke(car);
            }
        }
    }

    @Test
    @DisplayName("요구사항 4 - private field에 값 할당")
    void privateFieldAccess() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(car, "소나타");

        Field priceField = clazz.getDeclaredField("price");
        priceField.setAccessible(true);
        priceField.set(car, 30000000);

        assertEquals("소나타", car.getName());
        assertEquals(30000000, car.getPrice());
    }

    @Test
    @DisplayName("요구사항 5 - 인자를 가진 생성자의 인스턴스 생성")
    void constructorWithArgs() throws Exception {
        Class<Car> clazz = Car.class;
        Constructor<Car> constructor = clazz.getDeclaredConstructor(String.class, int.class);
        Car car = constructor.newInstance("아반떼", 20000000);

        assertEquals("아반떼", car.getName());
        assertEquals(20000000, car.getPrice());
    }
}
