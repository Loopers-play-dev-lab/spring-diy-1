package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionTest {

    @Test
    @DisplayName("요구사항 1 - Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println(carClass.getName());
        System.out.println(Arrays.toString(carClass.getDeclaredFields()));
        System.out.println(Arrays.toString(carClass.getDeclaredConstructors()));
        System.out.println(Arrays.toString(carClass.getDeclaredMethods()));
    }

    @Test
    @DisplayName("요구사항 2 - test로 시작하는 메소드 실행")
    void testMethodRun() throws InvocationTargetException, IllegalAccessException {
        Car car = new Car("소나타", 1000);

        for(Method method : Car.class.getDeclaredMethods()) {
            if(method.getName().startsWith("test")) {
                System.out.println(method.invoke(car));
            }
        }
    }

    @Test
    @DisplayName("요구사항 3 - @PrintView 애노테이션 메소드 실행")
    void testAnnotationMethodRun() throws InvocationTargetException, IllegalAccessException {
        Car car = new Car("소나타", 1000);

        Method testMethod = Arrays.stream(Car.class.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(PrintView.class)).findFirst().orElseThrow();
        testMethod.invoke(car);
    }

    @Test
    @DisplayName("요구사항 4 - private field에 값 할당")
    void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class<Car> clazz = Car.class;

        Car car = clazz.getDeclaredConstructor().newInstance();

        Field nameField = clazz.getDeclaredField("name");
        Field priceField = clazz.getDeclaredField("price");

        try {
            nameField.setAccessible(true);
            priceField.setAccessible(true);

            nameField.set(car, "소나타");
            priceField.set(car, 1000);
        } finally {
            nameField.setAccessible(false);
            priceField.setAccessible(false);
        }

        System.out.println(clazz.getDeclaredMethod("getName").invoke(car));
        System.out.println(clazz.getDeclaredMethod("getPrice").invoke(car));
    }

    @Test
    @DisplayName("요구사항 5 - 인자를 가진 생성자의 인스턴스 생성")
    void constructorWithArgs() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;

        Car car = clazz.getDeclaredConstructor(String.class, int.class).newInstance("소나타", 1000);

        System.out.println(clazz.getDeclaredMethod("getName").invoke(car));
        System.out.println(clazz.getDeclaredMethod("getPrice").invoke(car));
    }


}
