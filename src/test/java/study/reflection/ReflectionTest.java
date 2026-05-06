package study.reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReflectionTest {

    private static final String TEST_PREFIX = "test : ";

    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() throws Exception {
        Class<Car> carClass = Car.class;
        System.out.println("carClass.getName() = " + carClass.getName());
    }

    @Test
    @DisplayName("Car 메서드 호출하기")
    void testMethodRun() throws Exception {
        Class<Car> carClass = Car.class;
        List<Method> list = Arrays.stream(carClass.getMethods()).filter(method -> method.getName().startsWith("test")).toList();

        Car car = new Car();
        for (Method method : list) {
            String result = (String) method.invoke(car);
            Assertions.assertTrue(result.startsWith("test"));
        }

    }

    @Test
    @DisplayName("Car 객체에서 printView Annotation 붙은 메서드 호출하기")
    void testAnnotationMethodRun() throws Exception {
        List<Method> list = Arrays.stream(Car.class.getMethods()).filter(method -> method.isAnnotationPresent(PrintView.class)).toList();
        for (Method method : list) {
            Assertions.assertEquals(method, Car.class.getMethod("printView"));
            method.invoke(new Car());
        }
    }

    @Test
    @DisplayName("private field 붙은 대상 값 교체 하기")
    public void privateFieldAccess() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = new Car();
        String changeName = "changed";
        int changePrice = 10000;

        Map<String, Field> fieldMap = Arrays.stream(clazz.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        fieldMap.get("name").set(car, changeName);
        fieldMap.get("price").set(car, changePrice);

        Assertions.assertEquals(TEST_PREFIX + changeName, car.testGetName());
        Assertions.assertEquals(TEST_PREFIX + changePrice, car.testGetPrice());
    }

    @Test
    void constructorWithArgs() throws Exception {
        Class<Car> clazz = Car.class;
        Constructor declaredConstructor = clazz.getDeclaredConstructor(String.class, int.class);
        Object instance = declaredConstructor.newInstance("car", 100);
        if(instance instanceof Car car){
            Assertions.assertEquals(TEST_PREFIX + "car", car.testGetName());
            Assertions.assertEquals(TEST_PREFIX + 100, car.testGetPrice());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
