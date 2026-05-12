package com.diy.app.learning.reflection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReflectionTest {

    @Test
    @DisplayName("Lecture 생성자 찾기")
    void findConstructors() {
        Constructor<?>[] constructors = Lecture.class.getDeclaredConstructors();

        assertEquals(2, constructors.length);

        Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));

        assertEquals(2, constructors[0].getParameterCount());
        assertEquals(3, constructors[1].getParameterCount());
    }

    @Test
    @DisplayName("Lecture 인스턴스 동적 생성")
    void createInstance() throws Exception {
        Constructor<Lecture> constructor = Lecture.class.getDeclaredConstructor(String.class, int.class);
        Lecture lecture = constructor.newInstance("Spring", 100_000);

        assertEquals("Spring", lecture.getName());
        assertEquals(100_000, lecture.getPrice());
    }

    @Test
    @DisplayName("private 메서드 찾기")
    void findPrivateMethod() throws Exception {
        Method method = Lecture.class.getDeclaredMethod("changeVisible");

        assertNotNull(method);
        assertTrue(Modifier.isPrivate(method.getModifiers()));
    }

    @Test
    @DisplayName("private 메서드 호출")
    void invokePrivateMethod() throws Exception {
        Constructor<Lecture> constructor = Lecture.class.getDeclaredConstructor(String.class, int.class);
        Lecture lecture = constructor.newInstance("Spring", 100_000);

        Field field = Lecture.class.getDeclaredField("visible");
        field.setAccessible(true);

        assertFalse((boolean) field.get(lecture));

        Method method = Lecture.class.getDeclaredMethod("changeVisible");
        method.setAccessible(true);
        method.invoke(lecture);

        assertTrue((boolean) field.get(lecture));
    }

    @Test
    @DisplayName("애너테이션으로 메서드 찾기")
    void findMethodsByAnnotation() {
        List<Method> annotatedMethods = Arrays.stream(Lecture.class.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MethodOrder.class))
                .sorted(Comparator.comparing(Method::getName))
                .toList();

        assertEquals(2, annotatedMethods.size());
        assertEquals("getName", annotatedMethods.get(0).getName());
        assertEquals("getPrice", annotatedMethods.get(1).getName());
    }

    @Test
    @DisplayName("@MethodOrder 애너테이션 정보 조회")
    void getMethodOrderAnnotationInfo() {
        List<Method> annotatedMethods = Arrays.stream(Lecture.class.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MethodOrder.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(MethodOrder.class).value()))
                .toList();

        assertEquals(2, annotatedMethods.size());
        assertEquals(1, annotatedMethods.get(0).getAnnotation(MethodOrder.class).value());
        assertEquals(2, annotatedMethods.get(1).getAnnotation(MethodOrder.class).value());
    }
}
