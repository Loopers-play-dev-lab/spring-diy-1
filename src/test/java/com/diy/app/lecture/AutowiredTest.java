package com.diy.app.lecture;

import com.diy.framework.web.beans.factory.BeanFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AutowiredTest {

    @Test
    @DisplayName("빈 주입 확인")
    void 빈주입확인() {
        BeanFactory beanFactory = new BeanFactory("com.diy");

        beanFactory.getBeans().forEach((type, instance) ->
                System.out.println(type.getSimpleName() + " → " + instance));
    }
}
