package study.reflection.lecture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LectureReflectionTest {
    @Test
    @DisplayName("요구사항 1 : Lecture 생성자 찾기")
    void findConstructor() {
        Constructor<?>[] constructors = Lecture.class.getDeclaredConstructors();
        Arrays.stream(constructors)
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("요구사항 2 : Lecture 인스턴스 동적 생성1")
    void createConstructor1() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final String name = "테스트 이름";
        final int price = 10000;
        Lecture lecture = Lecture.class.getDeclaredConstructor(String.class, int.class).newInstance(name, price);

        assertThat(lecture.getName()).isEqualTo(name);
        assertThat(lecture.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("요구사항 2 : Lecture 인스턴스 동적 생성2")
    void createConstructor2() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final String name = "테스트 이름";
        final int price = 10000;
        final boolean visible = false;
        Lecture lecture = Lecture.class.getDeclaredConstructor(String.class, int.class, boolean.class).newInstance(name, price, visible);

        assertThat(lecture.getName()).isEqualTo(name);
        assertThat(lecture.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("요구사항 3 : private 메서드 찾기")
    void findPrivateMethod() {
        Class<Lecture> clazz = Lecture.class;
        Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.accessFlags().contains(AccessFlag.PRIVATE)).forEach(System.out::println);
    }

    @Test
    @DisplayName("요구사항 4 : private 메서드 호출")
    void executePrivateMethod() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class<Lecture> clazz = Lecture.class;

        final String name = "테스트 이름";
        final int price = 10000;
        final boolean visible = false;
        Lecture lecture = clazz.getDeclaredConstructor(String.class, int.class, boolean.class).newInstance(name, price, visible);

        Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.accessFlags().contains(AccessFlag.PRIVATE))
                .forEach(method -> {
                    try {
                        method.setAccessible(true);
                        method.invoke(lecture);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } finally {
                        method.setAccessible(false);
                    }
                });

        Field field = clazz.getDeclaredField("visible");
        field.setAccessible(true);
        try {
            assertThat(field.getBoolean(lecture)).isEqualTo(true);
        } finally {
            field.setAccessible(false);
        }
    }


}
