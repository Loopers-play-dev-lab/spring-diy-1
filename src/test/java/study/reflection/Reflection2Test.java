package study.reflection;

import com.diy.framework.web.beans.annotation.PrintView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection2Test {
    @Test
    @DisplayName("Car test로 시작하는 메서드 실행")
    void testMethodRun() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> carClass = Car.class;
        Object carObj = carClass.getDeclaredConstructor().newInstance();
        for(Method method : carClass.getMethods()) {
            String methodName = method.getName();
            if(methodName.startsWith("test")) {
                method.invoke(carObj);
            }
        }
    }

    @Test
    @DisplayName("Car PrintView 어노테이션이 붙은 메서드 실행")
    void testAnnotationMethodRun() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> carClass = Car.class;
        Object carObj = carClass.getDeclaredConstructor().newInstance();
        for(Method method : carClass.getMethods()) {
            if(method.isAnnotationPresent(PrintView.class)) {
                System.out.println(method.invoke(carObj));
            }
        }
    }

    @Test
    @DisplayName("Car private filed에 값 저장")
    void privateFieldAccess() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> carClass = Car.class;
        Field name = carClass.getDeclaredField("name");
        Field price = carClass.getDeclaredField("price");
        name.setAccessible(true);
        price.setAccessible(true);

        Car carObj = carClass.getDeclaredConstructor().newInstance();
        name.set(carObj, "아우디");
        price.set(carObj, 10000);

        System.out.println(carObj.testGetName());
        System.out.println(carObj.testGetPrice());
    }

    @Test
    @DisplayName("Car 인자를 가진 생성자의 인스턴스 생성 ")
    void constructorWithArgs() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> carClass = Car.class;

        for(Constructor<?> constructor : carClass.getDeclaredConstructors()) {
            int filedCount = constructor.getParameterCount();
            if(filedCount == 0) {
                Car car = (Car) constructor.newInstance();
                car.printView();
                System.out.println(car.testGetName());
                System.out.println(car.testGetPrice());
            } else {
                Car car = (Car) constructor.newInstance("아우디", 1000);
                car.printView();
                System.out.println(car.testGetName());
                System.out.println(car.testGetPrice());
            }
        }
    }
}
