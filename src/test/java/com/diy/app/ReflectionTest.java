package com.diy.app;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import java.lang.reflect.Method;

public class ReflectionTest {

    @Test
    void lecture_생성자_찾기() {
        // Lecture 생성자들 가져오기
        Constructor<?>[] constructors = Lecture.class.getDeclaredConstructors();

        // 가져온 생성자 출력
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor);
        }
    }

    @Test
    void lecture_인스턴스_동적_생성() throws Exception {
        // Lecture 기본 생성자 가져오기
        Constructor<Lecture> constructor = Lecture.class.getDeclaredConstructor();

        // 가져온 생성자로 객체 생성
        Lecture lecture = constructor.newInstance();

        // 생성된 객체 출력
        System.out.println(lecture);
    }

    @Test
    void private_메서드_찾기() throws Exception {
        // private 메서드 찾기
        Method method = Lecture.class.getDeclaredMethod("getDisplayName");

        // 찾은 메서드 출력
        System.out.println(method);
    }

    @Test
    void private_메서드_호출() throws Exception {
        // Lecture 객체 생성
        Lecture lecture = new Lecture();

        // private 메서드 찾기
        Method method = Lecture.class.getDeclaredMethod("getDisplayName");

        // private 메서드 접근 허용
        method.setAccessible(true);

        // private 메서드 호출
        Object result = method.invoke(lecture);

        // 호출 결과 출력
        System.out.println(result);
    }
}
