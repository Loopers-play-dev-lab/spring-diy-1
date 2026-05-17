package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.factory.support.NamedRepository;
import com.diy.framework.web.beans.factory.support.NamedService;
import com.diy.framework.web.beans.factory.support.TestBeanClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationContextTest {

    @Test
    @DisplayName("명시한 이름으로 빈을 조회할 수 있다")
    void getBeanByName() {
        ApplicationContext applicationContext = new ApplicationContext("com.diy.framework.web.beans.factory.support");

        Object bean = applicationContext.getBean("namedRepository");

        assertNotNull(bean);
        assertSame(NamedRepository.class, bean.getClass());
    }

    @Test
    @DisplayName("타입으로 조회하면 같은 인스턴스를 반환한다")
    void getBeanByType() {
        ApplicationContext applicationContext = new ApplicationContext("com.diy.framework.web.beans.factory.support");

        NamedService namedService = applicationContext.getBean(NamedService.class);
        NamedRepository namedRepository = applicationContext.getBean(NamedRepository.class);

        assertSame(namedRepository, namedService.namedRepository);
    }

    @Test
    @DisplayName("같은 이름의 빈이 있으면 예외가 발생한다")
    void failWhenBeanNamesConflict() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> new ApplicationContext("com.diy.framework.web.beans.factory.conflict"));

        assertEquals("같은 이름의 빈이 이미 등록되어 있습니다: duplicatedBean", exception.getMessage());
    }

    @Test
    @DisplayName("@Bean 메서드로 등록한 빈을 이름과 타입으로 조회할 수 있다")
    void getBeanRegisteredByBeanMethod() {
        ApplicationContext applicationContext = new ApplicationContext("com.diy.framework.web.beans.factory.support");

        TestBeanClient beanByType = applicationContext.getBean(TestBeanClient.class);
        Object beanByName = applicationContext.getBean("testBeanClient");

        assertNotNull(beanByName);
        assertSame(beanByType, beanByName);
        assertSame(applicationContext.getBean(NamedRepository.class), beanByType.namedRepository);
    }
}
