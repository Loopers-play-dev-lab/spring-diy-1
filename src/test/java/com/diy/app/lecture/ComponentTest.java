package com.diy.app.lecture;

import com.diy.framework.Component;
import com.diy.framework.web.beans.factory.BeanScanner;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ComponentTest {
    @Test
    @DisplayName("빈 생성 확인")
    void 빈_생성_확인() throws Exception {
        BeanScanner scanner = new BeanScanner("com.diy");
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> clazz : classes) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            System.out.println("생성된 빈: " + instance.getClass().getName());
        }
    }


}
