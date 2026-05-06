package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

public class LectureReflectionTest {

    @Test
    @DisplayName("요구사항 1 - Lecture 생성자 찾기")
    void findConstructors() {
        final Class<Lecture> clazz = Lecture.class;

        for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            System.out.println("constructor: " + constructor.getName()
                    + " params=" + Arrays.toString(constructor.getParameterTypes()));
        }
    }

    @Test
    @DisplayName("요구사항 2 - Lecture 인스턴스 동적 생성")
    void createInstance() throws Exception {
        final Class<Lecture> clazz = Lecture.class;

        final Constructor<?> twoArg = clazz.getDeclaredConstructor(String.class, int.class);
        final Lecture lecture1 = (Lecture) twoArg.newInstance("스프링 입문", 30_000);

        final Constructor<?> threeArg = clazz.getDeclaredConstructor(String.class, int.class, boolean.class);
        final Lecture lecture2 = (Lecture) threeArg.newInstance("DI 심화", 50_000, true);

        System.out.println("lecture1.name=" + lecture1.getName() + ", price=" + lecture1.getPrice());
        System.out.println("lecture2.name=" + lecture2.getName() + ", price=" + lecture2.getPrice());
    }

    @Test
    @DisplayName("요구사항 3 - private 메서드 찾기")
    void findPrivateMethods() {
        final Class<Lecture> clazz = Lecture.class;

        for (final Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                System.out.println("private method: " + method.getName());
            }
        }
    }

    @Test
    @DisplayName("요구사항 4 - private 메서드 호출")
    void invokePrivateMethod() throws Exception {
        final Class<Lecture> clazz = Lecture.class;
        final Lecture lecture = (Lecture) clazz
                .getDeclaredConstructor(String.class, int.class)
                .newInstance("스프링 입문", 30_000);

        final Method changeVisible = clazz.getDeclaredMethod("changeVisible");
        changeVisible.setAccessible(true);
        changeVisible.invoke(lecture);

        final Field visibleField = clazz.getDeclaredField("visible");
        visibleField.setAccessible(true);
        final boolean visible = (boolean) visibleField.get(lecture);
        System.out.println("visible after changeVisible() = " + visible);
    }

    @Test
    @DisplayName("요구사항 5 - 애너테이션으로 메서드 찾기")
    void findMethodsByAnnotation() {
        final Class<Lecture> clazz = Lecture.class;

        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodOrder.class)) {
                System.out.println("@MethodOrder method: " + method.getName());
            }
        }
    }

    @Test
    @DisplayName("요구사항 6 - @MethodOrder 애너테이션 정보 조회 (value 순으로 정렬)")
    void readAnnotationValue() throws Exception {
        final Class<Lecture> clazz = Lecture.class;
        final Lecture lecture = (Lecture) clazz
                .getDeclaredConstructor(String.class, int.class)
                .newInstance("스프링 입문", 30_000);

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MethodOrder.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(MethodOrder.class).value()))
                .forEach(method -> {
                    try {
                        final int order = method.getAnnotation(MethodOrder.class).value();
                        final Object result = method.invoke(lecture);
                        System.out.println("order=" + order + ", " + method.getName() + " => " + result);
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
