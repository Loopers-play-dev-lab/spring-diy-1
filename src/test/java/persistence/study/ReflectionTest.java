package persistence.study;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.reflection.Car;
import study.reflection.PrintView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionTest {

    @DisplayName("Car 객체 정보 가져오기")
    @Test
    public void showClass() {
        Class<Car> carClass = Car.class;

        System.out.println("=== [FIELD INFO] ===");
        for (Field field : carClass.getDeclaredFields()) {
            System.out.println("name = " + field.getName());
            System.out.println("accessFlag = " + field.accessFlags());
            System.out.println("type = " + field.getType() + " \n");
        }

        System.out.println("=== [CONSTRUCTOR INFO] ===");
        for (Constructor<?> constructor : carClass.getConstructors()) {
            System.out.println("name = " + constructor.getName());
            System.out.println("accessFlag = " + constructor.accessFlags());
            System.out.println("parameterCount = " + constructor.getParameterCount());
            System.out.println("parameters = " + Arrays.toString(constructor.getParameters())  + " \n");
        }

        System.out.println("=== [METHOD INFO] ===");
        for (Method method : carClass.getMethods()) {
            System.out.println("name = " + method.getName());
            System.out.println("accessFlag = " + method.accessFlags());
            System.out.println("returnType = " + method.getReturnType());
            System.out.println("parameterCount = " + method.getParameterCount());
            System.out.println("parameters = " + Arrays.toString(method.getParameters()));
            System.out.println("annotationCount = " + method.getAnnotations().length);
            System.out.println("annotations = " + Arrays.toString(method.getAnnotations()) + " \n");
        }
    }
    
    @DisplayName("test로 시작하는 메소드 실행")
    @Test
    public void testMethodRun() throws Exception {
        Car car = Car.class.getDeclaredConstructor().newInstance();

        Arrays.stream(Car.class.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        Object result = method.invoke(car);
                        System.out.println(method.getName() + " ---> " + result);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    
    
    @DisplayName("@PrintView 어노테이션을 감지해서 출력")
    @Test
    public void testAnnotationMethodRun() throws Exception {
        Car car = Car.class.getDeclaredConstructor().newInstance();

        Arrays.stream(Car.class.getMethods())
                .filter(method -> method.isAnnotationPresent(PrintView.class))
                .forEach(method -> {
                    try {
                        method.invoke(car);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    
    @DisplayName("private field에 값 할당")
    @Test
    public void privateFieldAccess() throws Exception {
        // given
        final Car car = Car.class.getDeclaredConstructor().newInstance();
        final String expectedName = "소나타";
        final int expectedPrice = 100_000;

        for (Field field : Car.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType().equals(String.class)) {
                field.set(car, expectedName);
            } else if (field.getType().equals(int.class)) {
                field.setInt(car, expectedPrice);
            } else {
                throw new IllegalStateException("지원하지 않는 타입: " + field.getType());
            }
        }

        assertEquals(expectedName, car.getName());
        assertEquals(expectedPrice, car.getPrice());

        System.out.println(car);
    }
    
    @DisplayName("인자를 가진 생성자의 인스턴스 생성")
    @Test
    public void constructorWithArgs() throws Exception {
        Class<Car> carClass = Car.class;
        final String expectedName = "소나타";
        final int expectedPrice = 100_000;
        Car car = null;

        // given
        for (Constructor<?> constructor : carClass.getConstructors()) {
            if (constructor.getParameterCount() == 2) {
                car = (Car) constructor.newInstance(expectedName, expectedPrice);
                break;
            }
        }

        assertEquals(expectedName, car.getName());
        assertEquals(expectedPrice, car.getPrice());
    }
    
}
