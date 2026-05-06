import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class LectureTest {

    @Test
    @DisplayName("Lecture 생성자 찾기  & 인스턴스 동적 생성")
    void showAndInvokeConstructor() throws NoSuchMethodException {
        Class<Lecture> lectureClass = Lecture.class;

        Constructor<Lecture> constructor = lectureClass.getDeclaredConstructor(String.class, int.class);
        try{
            constructor.setAccessible(true);
            Lecture l = (Lecture) constructor.newInstance("Reflection 생성자 찾기", 0);
            System.out.println("Lecture 객체 생성 : "  + l + " , Class 정보 : " + l.getClass().getName());
            System.out.println("Lecture Name : "  + l.getName() + " , Price : " + l.getPrice());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    @Test
    @DisplayName("private 메소드 찾기 & 호출")
    void showAndInvokePrivateMethod() throws NoSuchMethodException {
        Lecture lecture = new Lecture("private 메소드 찾기", 1);

        System.out.println("Before Method isVisible : " + lecture.isVisible());
        Arrays.stream( lecture.getClass().getDeclaredMethods())
                .filter(method -> Modifier.isPrivate(method.getModifiers()))
                .forEach(method -> {
                    try {
                        method.setAccessible(true);
                        method.invoke(lecture);

                        System.out.println("After Method isVisible : " + lecture.isVisible());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } finally {
                        method.setAccessible(true);
                    }
                });

    }

    @Test
    @DisplayName("어노테이션으로 메소드 찾고 정보 조회하기")
    void findWithAnnotation() throws NoSuchMethodException {
        Lecture lecture = new Lecture("Annotation 정보 찾기", 1);

        Arrays.stream( lecture.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(MethodOrder.class))
                .forEach(m -> {
                    MethodOrder annotation = m.getAnnotation(MethodOrder.class);
                    System.out.println("Method 정보 : " + m.getName() + ", MethodOrder 정보 : " + annotation.value());
                });
    }
}
