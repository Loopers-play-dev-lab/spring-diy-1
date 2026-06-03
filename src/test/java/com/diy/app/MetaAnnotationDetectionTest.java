package com.diy.app;

import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.annotation.Component;
import com.diy.framework.context.annotation.Controller;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetaAnnotationDetectionTest {

    // @RestController → @Controller → @Component (2단계 메타) 클래스가 빈 스캔에 잡히는가
    @Test
    void restControllerIsScannedAsComponentViaMeta() {
        final BeanScanner scanner = new BeanScanner("com.diy.framework", "com.diy.app");
        final Set<Class<?>> components = scanner.scanClassesTypeAnnotatedWith(Component.class);

        assertTrue(components.contains(LectureControllerV3.class),
                "@RestController 단독 클래스가 @Component 메타 스캔에 잡혀야 함");
    }

    // findAnnotationOnBean이 @RestController 안의 @Controller를 메타애너테이션으로 찾는가
    @Test
    void findAnnotationOnBeanFollowsMetaToController() {
        final ApplicationContext context = new ApplicationContext("com.diy.app");
        final Controller controller = context.findAnnotationOnBean(new LectureControllerV3(null), Controller.class);

        assertNotNull(controller, "@RestController 안의 @Controller를 메타애너테이션으로 찾아야 함");
    }
}
