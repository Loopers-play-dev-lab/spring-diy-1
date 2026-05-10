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

//    @Test
    public void beanCreate() {
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

        if (beanFactory == null) return;

        Set<String> beanNames = beanFactory.getSingletonObjects().keySet();

        for (String beanName : beanNames) {
            Object beanInstance = ApplicationContext.getBean(beanName);
            assert beanInstance != null : beanName + "이 인스턴스 생성 되지 않음";

            try {
                Class<?> interfaceOrClass = Class.forName(beanName);
                boolean isTypeMatch = interfaceOrClass.isAssignableFrom(beanInstance.getClass());

                System.out.println(String.format("[검증] Key(%s) == Instance(%s) -> %b",
                        beanName, beanInstance.getClass().getSimpleName(), isTypeMatch));

                assert isTypeMatch;
            } catch (Exception e) {
                System.out.println("빈 인스턴스의 클래스 조회 실패 " + e.getMessage());
            }
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
        beanFactory.getBeanNamesByType().forEach((type, names) -> {
            System.out.println(type.getSimpleName() + " -> " + names);
        });
    }
}