package study.reflection;

import com.diy.app.Car;
import com.diy.app.Lecture;
import com.diy.app.MethodOrder;
import com.diy.app.PrintView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ReflectionTest {

    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        System.out.println(carClass.getName());
        System.out.println(Arrays.toString(carClass.getDeclaredFields()));
        System.out.println(Arrays.toString(carClass.getDeclaredConstructors()));
        System.out.println(Arrays.toString(carClass.getDeclaredMethods()));
    }

    @Test
    @DisplayName("test로 시작하는 메소드 실행")
    void testMethodRun() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                Object result = method.invoke(car);
                System.out.println(method.getName() + " 결과: " + result);
            }
        }
    }

    @Test
    @DisplayName("@PrintView 어노테이션 메서드 실행")
    void testAnnotationMethodRun() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PrintView.class)) {
                method.invoke(car);
            }
        }
    }

    @Test
    @DisplayName("private 필드에 값 할당")
    void privateFieldAccess() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(car, "소나타");

        Field priceField = clazz.getDeclaredField("price");
        priceField.setAccessible(true);
        priceField.set(car, 10000);

        System.out.println("name: " + car.getName());
        System.out.println("price: " + car.getPrice());
    }

    @Test
    @DisplayName("인자를 가진 생성자의 인스턴스 생성")
    void constructorWithArgs() throws Exception {
        Class<Car> clazz = Car.class;

        Constructor<Car> constructor = clazz.getDeclaredConstructor(String.class, int.class);
        Car car = constructor.newInstance("G80", 2000);

        System.out.println("name: " + car.getName());
        System.out.println("price: " + car.getPrice());
    }

    @Test
    @DisplayName("Lecture 생성자 찾기")
    void findLectureConstructors() {
        Class<Lecture> clazz = Lecture.class;

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            System.out.println("생성자: " + constructor.getName());
            System.out.println("파라미터: " + Arrays.toString(constructor.getParameterTypes()));
            System.out.println("---");
        }
    }

    @Test
    @DisplayName("Lecture 인스턴스 동적 생성")
    void createLectureInstance() throws Exception {
        Class<Lecture> clazz = Lecture.class;

        Constructor<Lecture> constructor = clazz.getDeclaredConstructor(String.class, int.class);
        Lecture lecture = constructor.newInstance("스프링 강의", 50000);

        System.out.println("name: " + lecture.getName());
        System.out.println("price: " + lecture.getPrice());
    }

    @Test
    @DisplayName("private 메서드 찾기")
    void findPrivateMethod() {
        Class<Lecture> clazz = Lecture.class;

        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                System.out.println("private 메서드: " + method.getName());
            }
        }
    }

    @Test
    @DisplayName("private 메서드 호출")
    void invokePrivateMethod() throws Exception {
        Class<Lecture> clazz = Lecture.class;

        Constructor<Lecture> constructor = clazz.getDeclaredConstructor(String.class, int.class);
        Lecture lecture = constructor.newInstance("스프링 강의", 50000);

        Method changeVisible = clazz.getDeclaredMethod("changeVisible");
        changeVisible.setAccessible(true);
        changeVisible.invoke(lecture);

        Field visibleField = clazz.getDeclaredField("visible");
        visibleField.setAccessible(true);
        System.out.println("visible: " + visibleField.get(lecture));
    }

    @Test
    @DisplayName("어노테이션으로 메서드 찾기")
    void findAnnotatedMethods() {
        Class<Lecture> clazz = Lecture.class;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodOrder.class)) {
                System.out.println("@MethodOrder 메서드: " + method.getName());
            }
        }
    }

    @Test
    @DisplayName("@MethodOrder 어노테이션 정보 조회")
    void getAnnotationValue() {
        Class<Lecture> clazz = Lecture.class;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodOrder.class)) {
                MethodOrder annotation = method.getAnnotation(MethodOrder.class);
                System.out.println(method.getName() + " -> order: " + annotation.value());
            }
        }
    }
}