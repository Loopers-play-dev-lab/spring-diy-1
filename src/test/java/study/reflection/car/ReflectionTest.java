package study.reflection.car;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

  @Test
  @DisplayName("요구사항 1 - Car 객체 정보 가져오기")
  void showClass() {
    Class<Car> clazz = Car.class;
    System.out.println(clazz.getName());
  }

  @Test
  @DisplayName("요구사항 2 - test로 시작하는 메서드 실행하기")
  void testMethodRun()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Class<Car> clazz = Car.class;
    Car car = clazz.getDeclaredConstructor().newInstance();

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.getName().startsWith("test")) {
        System.out.println(method.invoke(car));
      }
    }
  }

  @Test
  @DisplayName("요구사항 3 - PrintView 애노테이션 메소드 실행하기")
  void testAnnotation()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Class<Car> clazz = Car.class;
    Car car = clazz.getDeclaredConstructor().newInstance();

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(PrintView.class)) {
        method.invoke(car);
      }
    }
  }

  @Test
  @DisplayName("요구사항 4 - private field에 값 할당")
  void testPrivateFieldAccess()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
    Class<Car> clazz = Car.class;
    Car car = clazz.getDeclaredConstructor().newInstance();

    Field name = clazz.getDeclaredField("name");
    name.setAccessible(true);
    name.set(car, "벤츠");
    name.setAccessible(false);

    Field price = clazz.getDeclaredField("price");
    price.setAccessible(true);
    price.set(car, 1000);
    price.setAccessible(false);

    System.out.println(car.testGetName());
    System.out.println(car.testGetPrice());
  }

  @Test
  @DisplayName("요구사항 5 - 인자를 가진 생성자의 인스턴스 생성")
  void constructorWithArgs()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
    Class<Car> clazz = Car.class;
    Car car = clazz
        .getDeclaredConstructor(String.class, int.class)
        .newInstance("벤츠", 1000);

    System.out.println(car.testGetName());
    System.out.println(car.testGetPrice());
  }

}
