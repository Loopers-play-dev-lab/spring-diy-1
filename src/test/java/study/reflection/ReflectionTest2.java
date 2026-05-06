package study.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReflectionTest2 {

    @Test
    @DisplayName("요구사항1 -> Lecture 생성자 찾기")
    void TestGetConstructors() {
        Class<Lecture> lectureClass = Lecture.class;

        Constructor<?>[] declaredConstructors = lectureClass.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor.getName());
        }
    }

    @Test
    @DisplayName("요구사항2 -> Lecture 인스턴스 동적 생성")
    void TestInstance()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Lecture> lectureClass = Lecture.class;

        Constructor<Lecture> declaredConstructor = lectureClass.getDeclaredConstructor(String.class, int.class);
        Lecture lecture = declaredConstructor.newInstance("kim", 100);

        System.out.println(lecture.getPrice());
    }

    @Test
    @DisplayName("요구사항3 -> Private 메서드 찾기")
    void TestGetPrivateMethod() {
        Class<Lecture> lectureClass = Lecture.class;

        Method[] methods = lectureClass.getDeclaredMethods();
        for (Method method : methods) {
            if(method.getModifiers() == Modifier.PRIVATE){
                System.out.println(method.getName());
            }
        }
    }

    @Test
    @DisplayName("요구사항4 -> Private 메서드 호출")
    void TestCallPrivateMethod()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Class<Lecture> lectureClass = Lecture.class;

        Constructor<Lecture> declaredConstructor = lectureClass.getDeclaredConstructor(String.class, int.class);
        Lecture lecture = declaredConstructor.newInstance("kim", 100);

        Method[] methods = lectureClass.getDeclaredMethods();
        for (Method method : methods) {
            if(method.getModifiers() == Modifier.PRIVATE){
                method.setAccessible(true);
                method.invoke(lecture);
            }
        }
    }

    @Test
    @DisplayName("요구사항5 -> 애노테이션 메서드 호출")
    void TestGetAnnotatedMethod()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Lecture> lectureClass = Lecture.class;

        Constructor<Lecture> declaredConstructor = lectureClass.getDeclaredConstructor(String.class, int.class);
        Lecture lecture = declaredConstructor.newInstance("kim", 100);

        Method[] declaredMethods = lectureClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if(declaredMethod.isAnnotationPresent(MethodOrder.class)) {
                System.out.println(declaredMethod.invoke(lecture));
            }
        }
    }

    @Test
    @DisplayName("요구사항6 -> @MethodOrder 애너테이션 정보 조회")
    void TestGetMethodOrderAnnotationInfo() {
        Class<Lecture> lectureClass = Lecture.class;

        Method[] declaredMethods = lectureClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if(declaredMethod.isAnnotationPresent(MethodOrder.class)) {
                Annotation annotation = declaredMethod.getAnnotation(MethodOrder.class);
                System.out.println(annotation.annotationType().getName());
                System.out.println(annotation.annotationType().accessFlags());
            }
        }
    }

}
