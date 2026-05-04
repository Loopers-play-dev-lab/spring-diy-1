package persistence.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.reflection.Lecture;
import study.reflection.MethodOrder;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LectureTest {

    @DisplayName("Lecture 생성자 찾기")
    @Test
    public void findLectureConstructors() {
        Class<Lecture> lectureClass = Lecture.class;
        int length = lectureClass.getDeclaredConstructors().length;
        assertEquals(2, length);
    }

    @DisplayName("Lecture 인스턴스 동적 생성")
    @Test
    public void crateLecture() throws Exception {
        final String name = "lecture";
        final int price = 100;
        final boolean visible = true;

        Class<Lecture> lectureClass = Lecture.class;
        List<Lecture> lectures = new ArrayList<>();

        for (Constructor<?> constructor : lectureClass.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 2) {
                constructor.newInstance(name, price);
                lectures.add((Lecture) constructor.newInstance(name, price));
            } else if (constructor.getParameterCount() == 3) {
                constructor.newInstance(name, price, visible);
                lectures.add((Lecture) constructor.newInstance(name, price, visible));
            }
        }

        assertEquals(2, lectures.size());
    }

    @DisplayName("private 메서드 찾기")
    @Test
    public void findPrivateMethod() {
        // given
        Class<Lecture> lectureClass = Lecture.class;

        Arrays.stream(lectureClass.getDeclaredMethods())
                .filter(method -> method.accessFlags().contains(AccessFlag.PRIVATE))
                .forEach(method -> {
                    System.out.println("name = " + method.getName());
                    System.out.println("returnType = " + method.getReturnType().getSimpleName());
                });
    }
    
    @DisplayName("private 메서드 호출")
    @Test
    public void callPrivateMethod() throws Exception {
        Class<Lecture> lectureClass = Lecture.class;

        final String name = "lecture";
        final int price = 100;
        final boolean nonVisible = false;

        Lecture lecture = lectureClass.getDeclaredConstructor(String.class, int.class, boolean.class)
                .newInstance(name, price, nonVisible);

        Arrays.stream(lectureClass.getDeclaredMethods())
                .filter(method -> method.accessFlags().contains(AccessFlag.PRIVATE))
                .forEach(method -> {
                    try {
                        method.setAccessible(true);
                        method.invoke(lecture);
                        method.setAccessible(false);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

        Field field = lectureClass.getDeclaredField("visible");
        field.setAccessible(true);
        assertEquals(true, field.get(lecture));
        field.setAccessible(false);
    }

    @DisplayName("애너테이션으로 메서드 찾기")
    @Test
    public void findMethodsByAnnotation() {
        Class<Lecture> lectureClass = Lecture.class;

        Arrays.stream(lectureClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MethodOrder.class))
                .forEach(method -> {
                    System.out.println("name = " + method.getName());
                });
    }

    @DisplayName("@MethodOrder 애너테이션 정보 조회")
    @Test
    public void findInfoByAnnotation() {
        Class<Lecture> lectureClass = Lecture.class;

        Arrays.stream(lectureClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MethodOrder.class))
                .forEach(method -> {
                    System.out.println("name = " + method.getName());
                    System.out.println("annotationValue = " + method.getAnnotation(MethodOrder.class).value());
                    System.out.println("returnType = " + method.getReturnType().getSimpleName());
                });
    }
}
