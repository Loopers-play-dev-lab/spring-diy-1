package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionTest {

    @Test
    @DisplayName("요구사항 1 : Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println(carClass.getName());
    }

    @Test
    @DisplayName("요구사항 2 : test로 시작하는 메소드 실행하기")
    void executeMethodStartWithTest() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor(String.class, int.class).newInstance("차 이름", 100000);

        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
            .filter(m -> m.getName().startsWith("test"))
            .peek(m -> System.out.println(m.getName()))
            .forEach(m -> {
                try {
                    System.out.println(m.invoke(car));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Test
    @DisplayName("요구사항 3 : @PrintView 애노테이션 메소드 실행하기")
    void executeMethodHaveAnnotation() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor(String.class, int.class).newInstance("차 이름", 100000);

        Method[] methods = clazz.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(PrintView.class))
                .forEach(m -> {
                    try {
                        m.invoke(car);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
