package study.beans;

import com.diy.framework.web.beans.Component;
import com.diy.framework.web.beans.factory.BeanScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class BeanScannerTest {

    @Test
    @DisplayName("@Component 붙은 클래스를 찾아서 인스턴스 생성")
    void createBeanInstance() throws Exception {
        BeanScanner scanner = new BeanScanner("com.diy.app");
        Set<Class<?>> beanClasses = scanner.scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> clazz : beanClasses) {
            System.out.println("빈 클래스: " + clazz.getName());
            Object instance = clazz.getDeclaredConstructor().newInstance();
            System.out.println("빈 인스턴스: " + instance);
        }
    }
}
