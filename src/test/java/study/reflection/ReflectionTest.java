package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionTest {

    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println(carClass.getName());
    }

    @Test
    @DisplayName("메서드명에 test가 붙은 메서드만 실행")
    void testMethodRun()throws Exception{
        Class<Car> carClass = Car.class;
        Car car = carClass.getDeclaredConstructor(String.class, int.class).newInstance();

        Method[] methods = carClass.getDeclaredMethods();
        for(Method method : methods){
            if(method.getName().startsWith("test")){
                Object result = method.invoke(car);
                System.out.println(result);
            }
        }
    }

    @Test
    @DisplayName("PrintView 어노테이션이 붙은 메서드를 실행")
    void testAnnotationMethodRun()throws Exception{
        Class<Car> carClass = Car.class;
        Car car = carClass.getDeclaredConstructor(String.class, int.class).newInstance();

        Method[] methods = carClass.getDeclaredMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(PrintView.class)){
                method.invoke(car);
            }
        }
    }

    @Test
    @DisplayName("field에 값 할당하기")
    void privateFieldAccess()throws Exception{
        Class<Car> carClass = Car.class;
        Car car = carClass.getDeclaredConstructor(String.class, int.class).newInstance();

        Field field = carClass.getDeclaredField("name");
        field.setAccessible(true);
        field.set(car, "BMW");

        System.out.println(field.get(car));
    }

    @Test
    @DisplayName("인자가 있는 생성자를 찾아서 객체 생성하기")
    void constructorWithArgs()throws Exception{
        Class<Car> carClass = Car.class;
        Constructor<?>[] constructors = carClass.getDeclaredConstructors();

        for(Constructor<?> constructor : constructors){
            if(constructor.getParameterCount() > 0){
                Car car = (Car) constructor.newInstance("벤츠", 1000);
                Object nameResult = car.testGetName();
                Object priceResult = car.testGetPrice();
                System.out.println(nameResult);
                System.out.println(priceResult);
                break;
            }
        }
    }
}

