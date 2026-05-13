package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.study.Car;
import persistence.study.PrintView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class ReflectionTest {
    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println(carClass.getName());
    }

    @Test
    @DisplayName("Test로 시작하는 method 실행")
    void testMethodRun() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<Car> clazz = Car.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(clazz.newInstance());
            }
        }
    }

    @Test
    @DisplayName("PrintView 어노테이션이 있는 method 실행")
    void testAnnotationMethodRun() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<Car> clazz = Car.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PrintView.class)) {
                method.invoke(clazz.newInstance());
            }
        }
    }

    @Test
    @DisplayName("private field에 값 할당")
    void privateFieldAccess() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;
        Field field = clazz.getDeclaredField("name");
        field.setAccessible(true);

        Car car = clazz.getDeclaredConstructor().newInstance();
        field.set(car, "소나타");

        System.out.println(car.testGetName());
    }

    @Test
    @DisplayName("인자를 가진 생성자의 인스턴스 생성")
    void constructorWithArgs() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            if (constructor.getParameterCount() == 2) {
                Car car = (Car) constructor.newInstance("sonata", 3535);
                System.out.println(car.testGetName());
                System.out.println(car.testGetPrice());
            }
        }
    }
}