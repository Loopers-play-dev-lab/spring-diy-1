package com.diy.app.lecture;

import static org.junit.jupiter.api.Assertions.*;

import com.diy.app.lecture.entity.Lecture;
import com.diy.app.lecture.entity.MethodOrder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReflectionLectureTest {

    @Test
    @DisplayName("Lecture 생성자 찾기")
    void 생성자찾기(){
        Class<Lecture> lectureClass = Lecture.class;
        System.out.println(lectureClass.getName());
    }

    @Test
    @DisplayName("Lecture 인스턴스 동적 생성")
    void 인스턴스동적생성() throws Exception{
        Class<Lecture> lectureClass = Lecture.class;
        Object instance = lectureClass.getDeclaredConstructor(String.class, int.class, boolean.class).newInstance("스프링강의", 3000, true);

        assertNotNull(instance); //인스턴스 실제 생성 되었는지
        assertInstanceOf(Lecture.class, instance); //타입이 맞는지

        Lecture lecture = (Lecture) instance;
        assertAll(
                () -> assertEquals("스프링강의", lecture.getName()),
                () -> assertEquals(3000, lecture.getPrice()),
                () -> assertTrue(lecture.isVisible())
        );
    }

    @Test
    @DisplayName("private 필드 찾기")
    void 프라이빗필드찾기() throws Exception{
        Class<Lecture> lectureClass = Lecture.class;
        Object instance = lectureClass
                .getDeclaredConstructor(String.class, int.class, boolean.class)
                .newInstance("스프링강의", 3000, true);

        //좀더단순한방법..
        Field nameField = lectureClass.getDeclaredField("name");
        Field priceField = lectureClass.getDeclaredField("price");
        Field visibleField = lectureClass.getDeclaredField("visible");

        nameField.setAccessible(true);
        priceField.setAccessible(true);
        visibleField.setAccessible(true);

        assertAll(
                () -> assertEquals("스프링강의", nameField.get(instance)),
                () -> assertEquals(3000, priceField.get(instance)),
                () -> assertEquals(true, visibleField.get(instance))
        );
    }

    @Test
    @DisplayName("private 메서드 찾기 및 호출")
    void 프라이빗메소드찾기및호출() throws Exception{
        Class<Lecture> lectureClass = Lecture.class;
        Object instance = lectureClass
                .getDeclaredConstructor(String.class, int.class, boolean.class)
                .newInstance("스프링강의", 3000, true);

        Method changeVisible = lectureClass.getDeclaredMethod("changeVisible");
        changeVisible.setAccessible(true);
        changeVisible.invoke(instance);

        assertTrue(((Lecture) instance).isVisible());
    }

    @Test
    @DisplayName("애너테이션으로 메서드 찾기")
    void 애너테이션테스트() throws Exception{
        Class<Lecture> lectureClass = Lecture.class;
        Object instance = lectureClass
                .getDeclaredConstructor(String.class, int.class, boolean.class)
                .newInstance("스프링강의", 3000, true);

        for (Method method : lectureClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodOrder.class)) {
                System.out.println("메소드명: " + method.getName());
                method.invoke(instance);
            }
        }
    }

    @Test
    @DisplayName("@MethodOrder 애너테이션 정보 조회")
    void 애너테이션메서드정보조회() throws Exception {
        Class<Lecture> lectureClass = Lecture.class;
        Object instance = lectureClass
                .getDeclaredConstructor(String.class, int.class, boolean.class)
                .newInstance("스프링강의", 3000, true);

        for (Method method : lectureClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodOrder.class)) {
                MethodOrder annotation = method.getAnnotation(MethodOrder.class);
                Object result = method.invoke(instance);

                System.out.println("메서드명: " + method.getName());
                System.out.println("order: " + annotation.value());
                System.out.println("반환값: " + result);
                System.out.println("접근제어자: " + Modifier.toString(method.getModifiers()));
                System.out.println("파라미터: " + Arrays.toString(method.getParameterTypes()));
            }
        }
    }



}