package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ReflectionTest {

    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println(carClass.getName());

        Arrays.stream(carClass.getDeclaredFields()).forEach(it -> System.out.println("필드 = " + it));
        Arrays.stream(carClass.getDeclaredConstructors()).forEach(it -> System.out.println("생성자 = " + it));
        Arrays.stream(carClass.getDeclaredMethods()).forEach(it -> System.out.println("메소드 = " + it));

        System.out.println();
    }

    @Test
    public void testMethodRun() {
        Class<Car> carClass = Car.class;
        Arrays.stream(carClass.getDeclaredMethods()).filter(it -> it.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(carClass.getDeclaredConstructor().newInstance());
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    public void testAnnotationMethodRun() {
        Class<Car> carClass = Car.class;
        Arrays.stream(carClass.getDeclaredMethods()).filter(it -> it.isAnnotationPresent(PrintView.class))
                .forEach(method -> {
                    try {
                        method.invoke(carClass.getDeclaredConstructor().newInstance());
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    
    @Test
    public void privateFieldAccess() throws Exception {
        Class<Car> carClass = Car.class;
        Field name = carClass.getDeclaredField("name");
        Field price = carClass.getDeclaredField("price");
        name.setAccessible(true);
        price.setAccessible(true);

        Car car = carClass.getDeclaredConstructor().newInstance();
        name.set(car, "뭘쉐뒈스 붼츠");
        price.set(car, 2_000_000_000);

        System.out.println("내 애마 = " + car.getName() + ", 가격은 " + car.getPrice() + "원");

    }
    
    @Test
    public void constructorWithArgs() throws Exception {
        Class<Car> carClass = Car.class;
        Arrays.stream(carClass.getDeclaredConstructors()).forEach(constructor -> {
            try {
                int parameterCount = constructor.getParameterCount();

                Car car;
                if (parameterCount == 0) {
                    car = (Car) constructor.newInstance();
                } else {
                    car = (Car) constructor.newInstance("훼라뤼", 2_000_000_000);
                }

                System.out.println("내 애마 = " + car.getName() + ", 가격은 " + car.getPrice() + "원");
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
