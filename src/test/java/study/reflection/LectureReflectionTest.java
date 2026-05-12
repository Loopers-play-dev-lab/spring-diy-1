package study.reflection;

import com.diy.app.Lecture;
import com.diy.app.MethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class LectureReflectionTest {

    @Test
    @DisplayName("Lecture 생성자 불러오기")
    void getConstructor(){
        Class<Lecture> lectureClass = Lecture.class;
        Constructor<?>[] constructors = lectureClass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor);
        }
    }

    @Test
    @DisplayName("Lecture 인스턴스 동적 생성")
    void makeInstanceDynamically()throws Exception{
        Class<Lecture> lectureClass = Lecture.class;
        Lecture lecture = lectureClass.getDeclaredConstructor().newInstance();
        System.out.println(lecture);
    }

    @Test
    @DisplayName("Lecture의 private 메서드 찾기")
    void findPrivateMethod(){
        Class<Lecture> lectureClass = Lecture.class;
        Method[] methods = lectureClass.getDeclaredMethods();
        for (Method method : methods) {
            if(method.getModifiers() == Modifier.PRIVATE){
                System.out.println("메서드명 : " + method.getName());
            }
        }
    }

    @Test
    @DisplayName("Lecture의 private 메서드 호출")
    void invokePrivateMethod()throws Exception{
        Class<Lecture> lectureClass = Lecture.class;
        Lecture lecture = lectureClass.getDeclaredConstructor(String.class, int.class).newInstance("스프링강의", 99000);

        Method[] methods = lectureClass.getDeclaredMethods();
        for (Method method : methods) {
            if(method.getModifiers() == Modifier.PRIVATE){
                method.setAccessible(true);
                method.invoke(lecture);
                Field visible = lectureClass.getDeclaredField("visible");
                visible.setAccessible(true);
                System.out.println("visible : " + visible.get(lecture));
            }
        }
    }

    @Test
    @DisplayName("에너테이션으로 메서드 찾기")
    void findMethodByAnnotation()throws Exception{
        Class<Lecture> lectureClass = Lecture.class;
        Method[] methods = lectureClass.getDeclaredMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(MethodOrder.class)){
                System.out.println(method.getName());
            }
        }
    }

    @Test
    @DisplayName("@MethodOrder 애너테이션 정보 조회")
    void getAnnotationInfo()throws Exception{
        Class<Lecture> lectureClass = Lecture.class;
        Method[] methods = lectureClass.getDeclaredMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(MethodOrder.class)){
                MethodOrder methodOrder = method.getAnnotation(MethodOrder.class);
                System.out.println(method.getName());
                System.out.println(methodOrder.value());
            }
        }
    }

}
