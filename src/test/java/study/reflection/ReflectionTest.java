package study.reflection;

import com.diy.app.Main;
import com.diy.app.utils.ApplicationContext;
import com.diy.app.utils.BeanFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

public class ReflectionTest {

    // @Test
    public void privateFieldAccess() {
        Class<Car> clazz = Car.class;
        System.out.println(clazz.getName());
        System.out.println(clazz.getMethods()[0].isAnnotationPresent(PrintView.class));

        try {
            Constructor<Car>[] constructors = (Constructor<Car>[]) clazz.getDeclaredConstructors();
            constructors[1].setAccessible(true);
            Car init = constructors[1].newInstance("자동차", 100);
            constructors[1].setAccessible(false);
            init.printView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getBeanTest() {
        ApplicationContext.run(Main.class);
        Field field = null;
        try {
            field = ApplicationContext.class.getDeclaredField("beanFactory");
            field.setAccessible(true);
        } catch (Exception e) {
            System.out.println("ApplicationContext beanFactory필드 접근 실패 " + e.getMessage());
            return; // 필드 접근 실패 시 이후 로직 진행 불가
        }
        BeanFactory beanFactory = null;
        try {
            beanFactory = (BeanFactory) field.get(null);
        } catch (Exception e) {
            System.out.println("Application beanFactory 인스턴스 조회 실패 " + e.getMessage());
        } finally {
            field.setAccessible(false);
        }
        beanFactory.getSingletonObjects().forEach((type,names) -> {
            System.out.println(type + " -> " + names);
        });
        System.out.println("=".repeat(20));
        beanFactory.getTypeIndexMap().forEach((type, names) -> {
            System.out.println(type.getSimpleName() + " -> " + names);
        });
    }
}