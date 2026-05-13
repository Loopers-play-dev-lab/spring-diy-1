package com.diy.app.lecture;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.diy.app.lecture.service.LectureService;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.beans.factory.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ComponentTest {

    @Test
    @DisplayName("@Component 스캔 확인")
    void 컴포넌트_스캔_확인() {
        BeanScanner scanner = new BeanScanner("com.diy");
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);

        assertFalse(classes.isEmpty());
        classes.forEach(clazz -> System.out.println("스캔된 빈 후보: " + clazz.getName()));
    }

    @Test
    @DisplayName("이름으로 빈 조회")
    void 이름으로_조회() throws InvocationTargetException, IllegalAccessException {
        BeanFactory beanFactory = new BeanFactory("com.diy");
        Object bean = beanFactory.getBean("lectureService");
        assertThat(bean).isInstanceOf(LectureService.class);
    }
}
