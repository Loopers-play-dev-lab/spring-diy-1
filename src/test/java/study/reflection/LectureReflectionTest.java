package study.reflection;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LectureReflectionTest {

    @Test
    public void Lecture_생성자_찾기() throws Exception {
        Class<Lecture> lectureClass = Lecture.class;
        Arrays.stream(lectureClass.getDeclaredConstructors()).forEach(constructor -> {
            System.out.print("생성자 = " + constructor.getName() + "(");
            int parameterCount = constructor.getParameterCount();
            AtomicInteger nowCount = new AtomicInteger();
            Arrays.stream(constructor.getParameters()).forEach(parameter -> {
                System.out.print(parameter.getType());
                nowCount.getAndIncrement();
                if (nowCount.get() != parameterCount) System.out.print(", ");
            });
            System.out.println(")");
        });
    }

    @Test
    public void Lecture_인스턴스_동적_생성() throws Exception {
        String name = "Spring Framework DIY";
        int price = 99_000;

        // TODO: 검사하기 vs 지금처럼 예외로 두기 - 어떤 게 좋을지 고르기
        Class<Lecture> lectureClass = Lecture.class;
        Constructor<?>[] declaredConstructors = lectureClass.getDeclaredConstructors();

        for (Constructor<?> declaredConstructor : declaredConstructors) {
            Optional<Lecture> lecture;
            try {
                lecture = Optional.of((Lecture) declaredConstructor.newInstance(name, price));
                System.out.println(lecture);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            catch (IllegalArgumentException ignored) {
//                lecture = Optional.empty();
            }
        }
    }

    @Test
    public void Lecture_private_메소드_찾기() throws Exception {
        Class<Lecture> lectureClass = Lecture.class;

        Arrays.stream(lectureClass.getDeclaredMethods())
                .filter(method -> method.getModifiers() == Modifier.PRIVATE)
                .forEach(it -> System.out.println(it.getName()));
    }

    @Test
    public void Lecture_private_메소드_호출() throws Exception {
        Class<Lecture> lectureClass = Lecture.class;

        String name = "Spring Framework DIY";
        int price = 99_000;
        boolean visible = true;
        Lecture lecture = new Lecture(name, price, visible);

        Arrays.stream(lectureClass.getDeclaredMethods())
                .filter(method -> method.getModifiers() == Modifier.PRIVATE)
                .forEach(it -> {
                    try {
                        it.setAccessible(true);
                        it.invoke(lecture);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


    @Test
    public void Lecture_애너테이션으로_매서드_찾기() throws Exception {
        Class<Lecture> lectureClass = Lecture.class;

        Class<MethodOrder> methodOrderClass = MethodOrder.class;

        Arrays.stream(lectureClass.getDeclaredMethods()).forEach(method -> {
            MethodOrder annotation = method.getDeclaredAnnotation(methodOrderClass);
            if (annotation != null)
                System.out.println("annotation = " + annotation + ", @MethodOrder가 있는 method = " + method);
        });
    }

    @Test
    public void 애너테이션_MethodOrder_정보_조회() throws Exception {
        Class<Lecture> lectureClass = Lecture.class;

        Class<MethodOrder> methodOrderClass = MethodOrder.class;

        Method[] declaredMethods = methodOrderClass.getDeclaredMethods();
        Field[] declaredFields = methodOrderClass.getDeclaredFields();
        Constructor<?>[] declaredConstructors = methodOrderClass.getDeclaredConstructors();
        Annotation[] declaredAnnotations = methodOrderClass.getDeclaredAnnotations();

        List<String> methodsString = Arrays.stream(declaredMethods).map(Method::toString).toList();
        List<String> fieldsString = Arrays.stream(declaredFields).map(Field::toString).toList();
        List<String> constructorsString = Arrays.stream(declaredConstructors).map(Constructor::toString).toList();
        List<String> annotationsString = Arrays.stream(declaredAnnotations).map(Annotation::toString).toList();

        String printString = methodsString +
                "\n" +
                fieldsString +
                "\n" +
                constructorsString +
                "\n" +
                annotationsString;

        System.out.println(printString);

        Arrays.stream(lectureClass.getDeclaredMethods()).forEach(method -> {
            MethodOrder annotation = method.getDeclaredAnnotation(methodOrderClass);
            if (annotation != null)
                System.out.println("annotation = " + annotation + ", value = " + annotation.value() + ", @MethodOrder가 있는 method = " + method);
        });
    }
}
