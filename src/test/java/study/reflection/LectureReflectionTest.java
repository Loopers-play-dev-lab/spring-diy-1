package study.reflection;

import com.diy.app.domain.Lecture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class LectureReflectionTest {
    @Test
    void findLectureConstructors() {
        Class<Lecture> lectureClass = Lecture.class;
        Constructor<?>[] declaredConstructors =
                lectureClass.getDeclaredConstructors();
        Assertions.assertEquals(1, declaredConstructors.length);
        Constructor<?> declaredConstructor = declaredConstructors[0];
        Assertions.assertEquals(3, declaredConstructor.getParameterCount());
    }
}
