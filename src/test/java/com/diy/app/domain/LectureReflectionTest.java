package com.diy.app.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

class LectureReflectionTest {
    @Test
    @DisplayName("생성자 찾기")
    void findConstructor() {
        Class<Lecture> lectureClass = Lecture.class;
        try {
            Constructor<Lecture> constructor = lectureClass.getDeclaredConstructor(new Class[]{String.class, String.class, int.class});
            System.out.println(constructor.getName());

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("인스턴스 동적 생성")
    void createInstanceDynamically() {
        Class<Lecture> lectureClass = Lecture.class;
        try {
            Constructor<Lecture> constructor = lectureClass.getDeclaredConstructor(new Class[]{String.class, String.class, int.class});
            Lecture lecture = constructor.newInstance(UUID.randomUUID().toString(), "test", 1111);
            System.out.println(constructor.getName());

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("private 메서드 찾고 호출하기")
    void findAndInvokePrivateMethod() {
        Class<Lecture> lectureClass = Lecture.class;

        try {
            Constructor<Lecture> constructor = lectureClass.getDeclaredConstructor(String.class, String.class, int.class);
            Lecture lecture = constructor.newInstance(UUID.randomUUID().toString(), "test", 1111);
            for (Method method : lectureClass.getDeclaredMethods()) {
                if (method.canAccess(lecture)) {
                    continue;
                }

                method.setAccessible(true);

                System.out.println(method.getName());

                method.invoke(lecture);
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("애너테이션으로 메서드 찾기")
    void findByAnnotation() {
        System.out.println(Arrays.stream(Lecture.class.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(MethodOrder.class)).toList().stream().map(Method::getName).toList());
    }

    @Test
    @DisplayName("@MethodOrder 애너테이션 정보 조회")
    void findMethodOrderInfo() {
        for (Method method : Lecture.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodOrder.class)) {
                MethodOrder annotation = method.getAnnotation(MethodOrder.class);
                System.out.println("method order : " + annotation.value());
                System.out.println("method name : " + method.getName());
            }
        }
    }
}