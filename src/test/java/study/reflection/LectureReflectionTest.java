package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class LectureReflectionTest {

    @DisplayName("생성자 찾기")
    @Test
    void findConstructor() {
        final Class<Lecture> lectureClass = Lecture.class;
        final Constructor<?>[] constructors = lectureClass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("constructor = " + constructor);
        }
    }

    @DisplayName("인스턴스 동적 생성")
    @Test
    void newInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Class<Lecture> lectureClass = Lecture.class;
        final Constructor<Lecture> constructor = lectureClass.getDeclaredConstructor(String.class, int.class);
        final Lecture lecture = constructor.newInstance("Lecture test", 99_000);
        System.out.println("[newInstance] " + lecture.getName() + " / " + lecture.getPrice());
    }

    @DisplayName("private 메서드 찾기")
    @Test
    void findPrivateMethod() {
        final Class<Lecture> lectureClass = Lecture.class;
        final Method[] methods = lectureClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.accessFlags().contains(AccessFlag.PRIVATE)) {
                System.out.println("private method: " + method.getName());
            }
        }
    }

    @DisplayName("private 메서드 호출")
    @Test
    void invokePrivateMethod() throws InvocationTargetException, IllegalAccessException {
        final Lecture lecture = new Lecture("test lecture", 99_000);
        final Class<? extends Lecture> lectureClass = lecture.getClass();
        final Method[] methods = lectureClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.accessFlags().contains(AccessFlag.PRIVATE)) {
                System.out.println("before lecture visible: " + lecture.isVisible());
                method.setAccessible(true);
                method.invoke(lecture);
                System.out.println("after lecture visible: " + lecture.isVisible());
                method.setAccessible(false);
            }
        }
    }

    @DisplayName("애너테이션이 붙어있는 메서드 찾기")
    @Test
    void findMethodByAnnotation() {
        final Class<Lecture> lectureClass = Lecture.class;
        final Method[] methods = lectureClass.getDeclaredMethods();
        for (Method method : methods) {
            final Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (Annotation declaredAnnotation : declaredAnnotations) {
                System.out.println(method.getName() + ": " + declaredAnnotation);
            }
        }
    }

    @DisplayName("@MethodOrder가 붙어있는 메서드 실행")
    @Test
    void invokeMethodByMethodOrderAnnotation() throws InvocationTargetException, IllegalAccessException {
        final Lecture lecture = new Lecture("test lecture", 99_000);
        final Class<? extends Lecture> lectureClass = lecture.getClass();
        final Method[] methods = lectureClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MethodOrder.class)) {
                System.out.println("invoke method " + method.getName() + ": " + method.invoke(lecture));
            }
        }
    }

    @DisplayName("@MethodOrder 정보 조회")
    @Test
    void showMethodOrderAnnotation() {
        final Class<MethodOrder> methodOrderClass = MethodOrder.class;
        System.out.println("methodOrderClass.isAnnotation(): " + methodOrderClass.isAnnotation());
        System.out.println("methodOrderClass.getDeclaredAnnotations(): " + Arrays.toString(methodOrderClass.getDeclaredAnnotations()));
        final Method[] methods = methodOrderClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("method.getName(): " + method.getName());
        }
    }
}
