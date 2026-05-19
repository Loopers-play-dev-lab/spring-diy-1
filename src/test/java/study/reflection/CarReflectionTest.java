package study.reflection;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.reflect.AccessFlag.PRIVATE;

class CarReflectionTest {

    @DisplayName("Car class 정보 출력")
    @Test
    void showClass() {
        Class<Car> carClass = Car.class;
        final PrintStream origin = System.out;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(baos)) {
            System.setOut(capture);
            System.out.print(carClass.getName());
        } finally {
            System.setOut(origin);
        }
        Assertions.assertEquals("study.reflection.Car", baos.toString());
    }

    @DisplayName("test로 시작하는 메소드 실행")
    @Test
    void testMethodRun() throws InvocationTargetException, IllegalAccessException {
        final Car car = new Car("my-car", 3_000);
        final Class<Car> carClass = Car.class;
        for (Method method : carClass.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                System.out.println(method.getName() + ": " + method.invoke(car));
            }
        }
    }

    @DisplayName("@PrintView가 붙어있는 메소드 실행")
    @Test
    void testAnnotationMethodRun() throws InvocationTargetException, IllegalAccessException {
        Car car = new Car("my-car", 3_000);
        final Class<Car> carClass = Car.class;
        for (Method method : carClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PrintView.class)) {
                method.invoke(car);
            }
        }
    }

    @DisplayName("private field에 값 할당")
    @Test
    void privateFieldAccess() throws IllegalAccessException {
        Car car = new Car();
        System.out.println("[privateFieldAccess::before] " + car.getName() + " / " + car.getPrice());
        final Class<Car> carClass = Car.class;
        final Field[] fields = carClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.accessFlags().contains(PRIVATE)) {
                continue;
            }
            field.setAccessible(true);
            if (field.getType() == String.class) {
                field.set(car, "소나타");
            }
            if (field.getType() == int.class) {
                field.set(car, 3_000);
            }
            field.setAccessible(false);
        }
        System.out.println("[privateFieldAccess::after] " + car.getName() + " / " + car.getPrice());
    }

    @DisplayName("인자를 가진 생성자 인스턴스 생성")
    @Test
    void constructorWithArgs() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Class<Car> carClass = Car.class;
        final Car car = carClass.getConstructor(String.class, int.class)
                                .newInstance("소나타", 3_000);
        System.out.println("[constructorWithArgs] " + car.getName() + " / " + car.getPrice());
    }
}
