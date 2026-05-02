package persistence.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.reflection.Car;
import study.reflection.PrintView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ReflectionTest {

    @DisplayName("Car 객체 정보 가져오기")
    @Test
    public void showClass() {
        // given
        Class<Car> carClass = Car.class;

        // when
        System.out.println(carClass.getName());

        for (Field field : carClass.getFields()) {
            System.out.println("Filed = " + field.getName());
        }

        for (Constructor constructor : carClass.getConstructors()) {
            System.out.println("constructor = " + constructor.getModifiers());
        }

        // then
    }
    
    @DisplayName("@PrintView 어노테이션을 감지해서 출력")
    @Test
    public void printTestView() throws Exception {
        var car = Car.class.getDeclaredConstructor().newInstance();

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
}
