package com.diy.framework.web.beans.factory;

import com.diy.app.LectureController;
import com.diy.app.LectureRepository;
import com.diy.framework.web.context.ApplicationContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.diy.framework.web.context.annotation.Component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ApplicationContextTest {

    @Component
    static class NoDefaultConstructorBean {
        public NoDefaultConstructorBean(String a) {}
        public NoDefaultConstructorBean(String a, int b) {}
    }

    @Test
    @DisplayName("생성자가 1개면 해당 생성자로 빈 생성")
    void getBean() throws Exception {
        ApplicationContext context = new ApplicationContext("com.diy.app");

        LectureRepository repo = context.getBean(LectureRepository.class);

        assertThat(repo).isInstanceOf(LectureRepository.class);
    }

    @Test
    @DisplayName("생성자가 여러 개인데 @Autowired도 기본 생성자도 없으면 에러")
    void noDefaultConstructorThrowsError() {
        assertThatThrownBy(() -> new ApplicationContext("com.diy.framework.web.beans.factory"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("@Autowired를 통한 빈 주입")
    void autowiredInjection() throws Exception {
        ApplicationContext context = new ApplicationContext("com.diy.app");

        LectureController controller = context.getBean(LectureController.class);

        assertThat(controller).isInstanceOf(LectureController.class);
    }
}
