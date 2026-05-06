package com.diy.study.reflection;

import com.diy.app.Lecture;
import com.diy.framework.web.annotation.MethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> clazz = Car.class;

        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        System.out.println(clazz.getName());
        Arrays.stream(clazz.getDeclaredFields()).forEach(System.out::println);

        String output = outputStream.toString();
        assertThat(output).contains("com.diy.study.reflection.Car");
        assertThat(output).contains("name");
        assertThat(output).contains("price");

        System.setOut(originalOutput);
    }

    @Test
    @DisplayName("testXXX 메서드의 실행을 확인")
    void executeTestMethod() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        for (var method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                Object result = method.invoke(car);
                assertThat((String) result).startsWith("test");
            }
        }
    }

    @Test
    @DisplayName("@PrintView 어노테이션이 붙은 메서드의 실행 확인")
    void testAnnotationMethodRun() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Method[] methods = clazz.getDeclaredMethods();

        for (var method : methods) {
            if (method.isAnnotationPresent(PrintView.class)) {
                method.invoke(car);
                assertThat(outputStream.toString()).contains("자동차 정보를 출력 합니다.");
            }
        }
        System.setOut(originalOutput);
    }

    @Test
    @DisplayName("private 필드 접근")
    void privateFieldAccess() throws Exception {
        Class<Car> clazz = Car.class;
        Car car = clazz.getDeclaredConstructor().newInstance();

        var nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(car, "Sonata");

        var priceField = clazz.getDeclaredField("price");
        priceField.setAccessible(true);
        priceField.set(car, new java.math.BigDecimal("30000"));

        assertThat(car.getName()).isEqualTo("Sonata");
        assertThat(car.getPrice()).isEqualTo(new java.math.BigDecimal("30000"));
    }

    @Test
    @DisplayName("인자를 가진 생성자의 인스턴스 생성")
    void constructorWithArgs() throws Exception {
        Class<Car> clazz = Car.class;

        var constructor = clazz.getDeclaredConstructor(String.class, java.math.BigDecimal.class);
        Car car = constructor.newInstance("소나타", new java.math.BigDecimal("30000"));

        assertThat(car.getName()).isEqualTo("소나타");
        assertThat(car.getPrice()).isEqualTo(new java.math.BigDecimal("30000"));
    }

    @Test
    @DisplayName("Lecture 생성자 찾기")
    void findLectureConstructors() {
        Class<Lecture> clazz = Lecture.class;

        var constructors = clazz.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    @DisplayName("Lecture 인스턴스 동적 생성")
    void createLectureInstance() throws Exception {
        Class<Lecture> clazz = Lecture.class;

        var constructor = clazz.getDeclaredConstructor(String.class, java.math.BigDecimal.class);
        constructor.setAccessible(true);

        Lecture lecture = constructor.newInstance("주데의 Spring DIY", new java.math.BigDecimal("100000"));

        assertThat(lecture.getName()).isEqualTo("주데의 Spring DIY");
        assertThat(lecture.getPrice()).isEqualTo(new java.math.BigDecimal("100000"));
    }

    @Test
    @DisplayName("@MethodOrder 애너테이션 정보 조회")
    void findMethodOrderAnnotation() {
        Class<Lecture> clazz = Lecture.class;

        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodOrder.class)) {
                MethodOrder annotation = method.getAnnotation(MethodOrder.class);

                if (method.getName().equals("getName")) {
                    assertThat(annotation.value()).isEqualTo(1);
                }
                if (method.getName().equals("getPrice")) {
                    assertThat(annotation.value()).isEqualTo(2);
                }
            }
        }
    }
}
