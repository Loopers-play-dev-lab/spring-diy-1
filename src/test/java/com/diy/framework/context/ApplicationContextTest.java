package com.diy.framework.context;

import com.diy.framework.context.fixture.autowired.Repository;
import com.diy.framework.context.fixture.autowired.Service;
import com.diy.framework.context.fixture.defaults.DefaultConstructorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class ApplicationContextTest {

    @Test
    @DisplayName("생성자가 1개면 해당 생성자로 빈을 생성한다.")
    void defaultConstructor() {
        ApplicationContext applicationContext = new ApplicationContext("com.diy.framework.context.fixture.defaults");
        applicationContext.initialize();

        assertThat(applicationContext.getBean(DefaultConstructorContext.class))
                .isInstanceOf(DefaultConstructorContext.class)
                .isNotNull();
    }

    @Test
    @DisplayName("@Autowired가 안붙은 기본 생성자만 여러개 존재하면 예외가 발생한다.")
    void multiNoAutowiredConstructor() {
        ApplicationContext applicationContext = new ApplicationContext("com.diy.framework.context.fixture.noautowired");

        assertThatThrownBy(applicationContext::initialize)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Autowired 생성자가 없습니다.");
    }

    @Test
    @DisplayName("생성자가 여러개 존재할때 @Autowired가 붙은 생성자를 우선으로 사용하여 빈을 생성한다.")
    void multiConstructor() {
        ApplicationContext applicationContext = new ApplicationContext("com.diy.framework.context.fixture.autowired");
        applicationContext.initialize();

        Object service = applicationContext.getBean(Service.class);
        Object repository = applicationContext.getBean(Repository.class);

        assertThat(service).isInstanceOf(Service.class).isNotNull();
        assertThat(((Service) service).getRepository()).isInstanceOf(repository.getClass()).isNotNull();
    }
}
