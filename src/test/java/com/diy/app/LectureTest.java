package com.diy.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

class LectureTest {

    @Test
    @DisplayName("Lecture의 생성자 찾기")
    void findLectureConstructor() {
        Class<Lecture> clazz = Lecture.class;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Arrays.stream(constructors).forEach(System.out::println);
    }

    @Test
    @DisplayName("Lecture 인스턴스 동적 생성")
    void createLectureInstanceDynamically() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Lecture> clazz = Lecture.class;
        Lecture lecture = clazz
            .getDeclaredConstructor(String.class, int.class)
            .newInstance("test1", 4000);
        System.out.println(lecture);
    }

    @Test
    @DisplayName("private 메서드 찾기")
    void findPrivateMethod() {
        Class<Lecture> clazz = Lecture.class;
        Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.getModifiers() == Modifier.PRIVATE)
            .forEach(System.out::println);
    }

    @Test
    @DisplayName("private 메서드 호출하기")
    void callPrivateMethod() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Lecture> clazz = Lecture.class;
        Lecture lecture = clazz
            .getDeclaredConstructor(String.class, int.class)
            .newInstance("test1", 4000);

        for(Method method: clazz.getDeclaredMethods()) {
            if (method.getModifiers() == Modifier.PRIVATE) {
                method.setAccessible(true);
                method.invoke(lecture);
                method.setAccessible(false);
            }
        }

    }

    @Test
    @DisplayName("Annotation으로 메서드 찾기")
    void findMethodByAnnotation() {
        Class<Lecture> clazz = Lecture.class;

        Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(MethodOrder.class))
            .forEach(System.out::println);
    }

    @Test
    @DisplayName("@MethodOrder 애너테이션 정보 조회")
    void getAnnotationInfo() {
        Class<Lecture> clazz = Lecture.class;
        Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(MethodOrder.class))
            .forEach(method -> System.out.println(method.getName() + ": " + method.getDeclaredAnnotation(MethodOrder.class)));
    }
}
