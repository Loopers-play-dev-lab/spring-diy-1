package study.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

    @Test
    @DisplayName("요구사항1 -> Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println(carClass.getName());
    }

    @Test
    @DisplayName("요구사항2 -> Car test로 시작하는 메소드를 자동 실행하기")
    void testMethodRun()
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<Car> clazz = Car.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {

            if(declaredMethod.getName().startsWith("test")) {
                Car car = clazz.getDeclaredConstructor().newInstance();
                System.out.println(declaredMethod.invoke(car));
            }
        }
    }

    @Test
    @DisplayName("요구사항3 -> @PrintView 애노테이션 메소드 실행")
    void testAnnotationMethodRun()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {

            if(declaredMethod.isAnnotationPresent(PrintView.class)) {
                Car car = clazz.getDeclaredConstructor().newInstance();
                System.out.println(declaredMethod.invoke(car));
            }
        }
    }

    @Test
    @DisplayName("요구사항4 -> private field에 값 할당")
    void privateFieldAccess()
        throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);

        Field price = clazz.getDeclaredField("price");
        price.setAccessible(true);

        Car car = clazz.getDeclaredConstructor().newInstance();
        name.set(car, "test");
        price.set(car, 100);

        System.out.println(car.testGetName());
        System.out.println(car.testGetPrice());
    }

    @Test
    @DisplayName("요구사항5 -> 인자를 가진 생성자의 인스턴스 생성")
    void constructorWithArgs()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;

        Constructor<Car> declaredConstructor = clazz.getDeclaredConstructor(String.class, int.class);
        Car car = declaredConstructor.newInstance("kim", 100);

        System.out.println(car.testGetName());
        System.out.println(car.testGetPrice());
    }
}
