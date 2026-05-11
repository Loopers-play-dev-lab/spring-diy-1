package study.reflection;

import com.diy.app.Entity.Lecture;
import com.diy.app.annotation.Component;
import com.diy.app.utils.BeanScanner;
import javassist.expr.Instanceof;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Set;

public class ReflectionTest {

//    @Test
    public void privateFieldAccess() {
        Class<Car> clazz = Car.class;
        System.out.println(clazz.getName());
        System.out.println(clazz.getMethods()[0].isAnnotationPresent(PrintView.class));
        try {
            Constructor<Car>[] constructors = (Constructor<Car>[]) clazz.getDeclaredConstructors();
            constructors[1].setAccessible(true);
            Car init = constructors[1].newInstance("자동차",100);
            constructors[1].setAccessible(false);
            init.printView();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void beanScan () {
        BeanScanner beanScanner = new BeanScanner("com.diy.app");
        beanScanner.createBean();
    }
}