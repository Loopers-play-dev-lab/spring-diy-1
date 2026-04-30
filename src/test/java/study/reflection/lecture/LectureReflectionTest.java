package study.reflection.lecture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
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
}
