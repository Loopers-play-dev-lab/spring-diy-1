package study.reflection;

import com.diy.app.Main;
import com.diy.app.annotation.Bean;
import com.diy.app.annotation.Configuration;
import com.diy.app.utils.ApplicationContext;
import com.diy.app.utils.BeanFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReflectionTest {

    private BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        ApplicationContext.run(Main.class);
        Field field = null;
        try {
            field = ApplicationContext.class.getDeclaredField("beanFactory");
            field.setAccessible(true);
            this.beanFactory = (BeanFactory) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("BeanFactory 추출 실패!", e);
        } finally {
            field.setAccessible(false);
        }

    }
    @Test
    @DisplayName("빈 저장소에 등록된 전체 빈 목록 출력")
    void printSingletonObjects() {
        System.out.println("\n=== [Singleton Objects Map] ===");
        beanFactory.getSingletonObjects().forEach((name, instance) ->
                System.out.printf("BeanName: %-20s | Instance: %s%n", name, instance.getClass().getSimpleName())
        );
    }

    @Test
    @DisplayName("타입별 인덱스에 등록된 빈 계층 구조 출력")
    void printTypeIndexMap() {
        System.out.println("\n=== [Type Index Map (Hierarchy)] ===");
        beanFactory.getTypeIndexMap().forEach((type, instances) ->
                System.out.printf("Type: %-30s | Instances: %s%n", type.getSimpleName(), instances)
        );
    }

    @Test
    @DisplayName("특정 빈 조회 검증 - @Bean 메서드로 생성된 객체")
    void checkSpecificBeanCreation() {
        Object defaultTest = beanFactory.getBean("defaultTest");
        Object nameTest = beanFactory.getBean("nameTest");
        Object valueTest = beanFactory.getBean("valueTest");

        assertEquals("defaultTest 생성됨",defaultTest);
        assertEquals("nameTest 생성됨",nameTest);
        assertEquals("valueTest 생성됨", valueTest);
    }

}