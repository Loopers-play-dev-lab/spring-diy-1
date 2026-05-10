package study.reflection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionStudyTest {
    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        PrintStream standardOut = System.out;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));

        Class<Car> carClass = Car.class;

        System.out.println(carClass.getSimpleName());

        Assertions.assertThat("Car").isEqualTo(byteArrayOutputStream.toString().trim());

        System.setOut(standardOut);
    }

    @Test
    @DisplayName("test로 시작하는 메서드 실행")
    void testMethodRun() {
        Class<Car> carClass = Car.class;
        Car car = null;
        Field name = null;
        Field price = null;
        try {
            car = carClass.getDeclaredConstructor().newInstance();
            name = carClass.getDeclaredField("name");
            price = carClass.getDeclaredField("price");
            name.setAccessible(true);
            price.setAccessible(true);
            name.set(car, "test");
            price.set(car, 1111);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Method method : carClass.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                method.setAccessible(true);
                try {
                    System.out.println(method.getName());

                    Object result = method.invoke(car);
                    System.out.println(result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    @DisplayName("어노테이션 메서드 실행")
    void testAnnotationMethodRun() {
        Class<Car> carClass = Car.class;
        Method method = Arrays.stream(carClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(PrintView.class)).findFirst().get();
        try {
            method.invoke(carClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("private field 값 할당")
    void privateFieldAccess() {
        Class<Car> carClass = Car.class;
        Car car = null;
        Field name = null;
        Field price = null;
        try {
            car = carClass.getDeclaredConstructor().newInstance();
            name = carClass.getDeclaredField("name");
            price = carClass.getDeclaredField("price");
            name.setAccessible(true);
            price.setAccessible(true);
            name.set(car, "test");
            price.set(car, 1111);

            System.out.println(car.testGetName());
            System.out.println(car.testGetPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("인자 가진 생성자 인스턴스 생성")
    void constructorWithArgs() {
        Class<Car> carClass = Car.class;

        try {
            Constructor<Car> cs = carClass.getDeclaredConstructor(new Class[]{String.class, int.class});
            Car car = (Car) cs.newInstance("test", 1111);

            System.out.println(car.testGetName());
            System.out.println(car.testGetPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
