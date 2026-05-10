package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class LectureReflectionTest {

    @Test
    @DisplayName("요구사항 1 - Lecture 생성자 찾기")
    void findConstructor() {
        Class<Lecture> lectureClass = Lecture.class;

        Constructor<?>[] constructors = lectureClass.getDeclaredConstructors();

        System.out.println(Arrays.toString(constructors));
    }

    @Test
    @DisplayName("요구사항 2 - Lecture 인스턴스 동적 생성")
    void createDynamicInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Lecture> lectureClass = Lecture.class;

        Lecture lecture1 = lectureClass.getDeclaredConstructor(String.class, int.class).newInstance("강의1", 1000);
        Lecture lecture2 = lectureClass.getDeclaredConstructor(String.class, int.class, boolean.class).newInstance("강의2", 2000, true);

        System.out.println("강의 1 : " + lecture1);
        System.out.println("강의 2 : " + lecture2);
    }

    @Test
    @DisplayName("요구사항 3, 4 - private 메서드 찾기 & private 메서드 호출")
    void findAndInvokePrivateMethod() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Lecture> lectureClass = Lecture.class;

        Lecture lecture = lectureClass.getDeclaredConstructor(String.class, int.class).newInstance("테스트", 1000);

        List<Method> privateMethods = Arrays.stream(lectureClass.getDeclaredMethods())
                .filter(method -> Modifier.isPrivate(method.getModifiers()))
                .toList();

        System.out.println("private 메서드 호출 전 lecture : " + lecture);

        for(Method method : privateMethods) {
            try {
                method.setAccessible(true);
                method.invoke(lecture);
                System.out.println("private 메서드 호출 후 lecture : " + lecture);
            } finally {
                method.setAccessible(false);
            }
        }
    }

    @Test
    @DisplayName("요구사항 5 - 애너테이션으로 메서드 찾기")
    void findMethodByAnnotation() {
        Class<Lecture> lectureClass = Lecture.class;

        List<Method> methods = Arrays.stream(lectureClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MethodOrder.class))
                .toList();

        System.out.println(methods);
    }

    @Test
    @DisplayName("요구사항 6 - @MethodOrder 애너테이션 정보 조회")
    void showAnnotation() {
        Class<Lecture> lectureClass = Lecture.class;

        Arrays.stream(lectureClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(MethodOrder.class))
                .forEach(m -> {
                    MethodOrder annotation = m.getAnnotation(MethodOrder.class);
                    System.out.println("annotationType : " + annotation.annotationType() + ", value : " + annotation.value());
                });
    }
}
