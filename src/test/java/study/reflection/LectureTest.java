package study.reflection;

import com.diy.framework.web.annotation.MethodOrder;
import com.diy.framework.web.annotation.PrintView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class LectureTest {
    @Test
    @DisplayName("Lecture 생성자 찾기")
    void findLectureConstructor() {
        Class<Lecture> carClass = Lecture.class;
        for(Constructor<?> constructor : carClass.getDeclaredConstructors()) {
            int filedCount = constructor.getParameterTypes().length;
            System.out.print(constructor.getName() + "(");
            for(int i = 0; i < filedCount; i++) {
                System.out.print(constructor.getParameters()[i].getType());
                if(i != filedCount - 1) System.out.print(", ");
            }
            System.out.println(")");
        }
    }

    @Test
    @DisplayName("Lecture 생성자 동적 생성")
    void createLectureConstructor() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Class<Lecture> lectureClass = Lecture.class;

        Lecture lecture1 = lectureClass
                .getDeclaredConstructor(String.class, int.class)
                .newInstance("강의1", 1000);
        System.out.println("lecture1");
        System.out.println(lecture1);

        Lecture lecture2 = lectureClass
                .getDeclaredConstructor(String.class, int.class, boolean.class)
                .newInstance("강의2", 1000, true);
        System.out.println("lecture2");
        System.out.println(lecture2);
    }

    @Test
    @DisplayName("Lecture private 메서드 찾기")
    void getLecturePrivateMethod(){
        Class<Lecture> lectureClass = Lecture.class;

        for(Method method : lectureClass.getDeclaredMethods()) {
            String methodName = method.getName();
            if(method.getModifiers() == Modifier.PRIVATE) {
                System.out.print(methodName + "(");
                Class<?>[] parameterTypes = method.getParameterTypes();
                for(int i = 0; i < parameterTypes.length; i++) {
                    System.out.print(parameterTypes[i].getName());
                    if(i != parameterTypes.length - 1) System.out.print(", ");
                }
                System.out.println(")");
            }
        }
    }

    @Test
    @DisplayName("Lecture private 메서드 실행")
    void activeLecturePrivateMethod() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Lecture> lectureClass = Lecture.class;

        Lecture lecture = lectureClass
                .getDeclaredConstructor(String.class, int.class)
                .newInstance("강의1", 1000);

        for(Method method : lectureClass.getDeclaredMethods()) {
            if(method.getModifiers() == Modifier.PRIVATE) {
                method.setAccessible(true);
                method.invoke(lecture);
            }
        }

        System.out.print("isVisible : " + lecture.getVisible());
    }

    @Test
    @DisplayName("Lecture 애너테이션으로 메서드 찾기")
    void getLectureAnnotationMethod(){
        Class<Lecture> lectureClass = Lecture.class;

        for(Method method : lectureClass.getDeclaredMethods()) {
            if(method.isAnnotationPresent(MethodOrder.class)) {
                System.out.println(method.getName());
            }
        }
    }

    @Test
    @DisplayName("Lecture 애너테이션으로 메서드 찾기")
    void getAnnotationInfo(){
        Class<Lecture> lectureClass = Lecture.class;

        for(Method method : lectureClass.getDeclaredMethods()) {
            if(method.isAnnotationPresent(MethodOrder.class)) {
                System.out.println(method.getName() + " " + method.getDeclaredAnnotation(MethodOrder.class));
            }
        }
    }
}
