package study.reflection;

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
}
