package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionTest {

    @Test
    @DisplayName("요구사항 1 - Car 클래스의 모든 필드/생성자/메서드 정보 출력")
    void showClass() {
        final Class<Car> carClass = Car.class;

        System.out.println("[Class] " + carClass.getName());

        System.out.println("[Fields]");
        for (final Field field : carClass.getDeclaredFields()) {
            System.out.println("  - " + field.getType().getSimpleName() + " " + field.getName());
        }

        System.out.println("[Constructors]");
        for (final Constructor<?> constructor : carClass.getDeclaredConstructors()) {
            System.out.println("  - " + constructor.getName()
                    + Arrays.toString(constructor.getParameterTypes()));
        }

        System.out.println("[Methods]");
        for (final Method method : carClass.getDeclaredMethods()) {
            System.out.println("  - " + method.getReturnType().getSimpleName()
                    + " " + method.getName()
                    + Arrays.toString(method.getParameterTypes()));
        }
    }

    @Test
    @DisplayName("요구사항 2 - test로 시작하는 메서드 자동 실행")
    void testMethodRun() throws Exception {
        final Class<Car> carClass = Car.class;
        final Car car = carClass.getDeclaredConstructor().newInstance();

        for (final Method method : carClass.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                final Object result = method.invoke(car);
                System.out.println(method.getName() + " => " + result);
            }
        }
    }

    @Test
    @DisplayName("요구사항 3 - @PrintView 애노테이션 메서드 자동 실행")
    void testAnnotationMethodRun() throws Exception {
        final Class<Car> carClass = Car.class;
        final Car car = carClass.getDeclaredConstructor().newInstance();

        for (final Method method : carClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PrintView.class)) {
                method.invoke(car);
            }
        }
    }

    @Test
    @DisplayName("요구사항 4 - private field 에 값 할당")
    void privateFieldAccess() throws Exception {
        final Class<Car> carClass = Car.class;
        final Car car = carClass.getDeclaredConstructor().newInstance();

        final Field nameField = carClass.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(car, "소나타");

        final Field priceField = carClass.getDeclaredField("price");
        priceField.setAccessible(true);
        priceField.set(car, 30_000_000);

        System.out.println("name  = " + car.getName());
        System.out.println("price = " + car.getPrice());
    }

    @Test
    @DisplayName("요구사항 5 - 인자를 가진 생성자로 인스턴스 생성")
    void constructorWithArgs() throws Exception {
        final Class<Car> carClass = Car.class;

        Constructor<?> targetConstructor = null;
        for (final Constructor<?> constructor : carClass.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length > 0) {
                targetConstructor = constructor;
                break;
            }
        }

        final Car car = (Car) targetConstructor.newInstance("아반떼", 20_000_000);
        System.out.println("name  = " + car.getName());
        System.out.println("price = " + car.getPrice());
    }
}
