package study.reflection.lecture;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

  @Test
  @DisplayName("1. Lecture 생성자 찾기")
  void findConstructor() {
    Class<Lecture> clazz = Lecture.class;
    for (Constructor c : clazz.getDeclaredConstructors()) {
      System.out.print(clazz.getSimpleName() + ":");
      for (Class param : c.getParameterTypes()) {
        System.out.print(" " + param.getSimpleName());
      }
      System.out.println();
    }
  }

  @Test
  @DisplayName("2. Lecture 인스턴스 동적 생성")
  void createDynamicInstance()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Class<Lecture> clazz = Lecture.class;
    Lecture lecture = clazz
        .getDeclaredConstructor(String.class, int.class)
        .newInstance("강의1", 1000);
    System.out.println(lecture);
  }

  @Test
  @DisplayName("3. private 메서드 찾기")
  void findPrivateMethod() {
    Class<Lecture> clazz = Lecture.class;
    for (Method method : clazz.getDeclaredMethods()) {
      if (method.getModifiers() == Modifier.PRIVATE) {
        System.out.println(method.getName());
      }
    }
  }

  @Test
  @DisplayName("4. private 메서드 호출")
  void callPrivateMethod()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Class<Lecture> clazz = Lecture.class;
    Lecture lecture = clazz
        .getDeclaredConstructor(String.class, int.class)
        .newInstance("강의1", 1000);

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.getModifiers() == Modifier.PRIVATE) {
        method.setAccessible(true);
        method.invoke(lecture);
        method.setAccessible(false);
      }
    }
  }

  @Test
  @DisplayName("5. 애너테이션으로 메서드 찾기")
  void findMethodByAnnotation() {
    Class<Lecture> clazz = Lecture.class;

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(MethodOrder.class)) {
        System.out.println(method.getName());
      }
    }
  }

  @Test
  @DisplayName("6. @MethodOrder 애너테이션 정보 조회")
  void constructorWithArgs() {
    Class<Lecture> clazz = Lecture.class;

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(MethodOrder.class)) {
        System.out.println(method.getName() + ": " + method.getDeclaredAnnotation(MethodOrder.class));
      }
    }
  }

}
