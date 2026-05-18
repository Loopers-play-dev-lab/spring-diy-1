package com.diy.framework.context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApplicationContextTest {

    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new ApplicationContext("com.diy.framework.context");
        context.initialize();
    }

    @Test
    @DisplayName("@Component로 등록된 Bean 조회")
    void getBeanRegisteredByComponent() {
        Foo foo = (Foo) context.getBean("foo");

        assertEquals("Foo Hello!", foo.hello());
    }

    @Test
    @DisplayName("@Bean으로 등록된 Bean 조회")
    void getBeanRegisteredByBeanAnnotation() {
        Foo foo = (Foo) context.getBean("methodBean");

        assertEquals("Bean Hello!", foo.hello());
    }
}
