package com.diy.framework.web.beans.factory;

import com.diy.app.LectureController;
import com.diy.app.LectureRepository;
import com.diy.framework.web.context.ApplicationContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ApplicationContextTest {

    @Test
    @DisplayName("생성자가 1개면 해당 생성자로 빈 생성")
    void getBean() {
        ApplicationContext context = new ApplicationContext("com.diy");

        Object repo = context.getBean("LectureRepository");

        assertThat(repo).isInstanceOf(LectureRepository.class);
    }

    @Test
    @DisplayName("생성자가 여러 개인데 @Autowired도 기본 생성자도 없으면 에러")
    void noDefaultConstructorThrowsError() {
        assertThatThrownBy(() -> new ApplicationContext("com.test.fixture"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("@Controller 어노테이션을 통한 빈 등록")
    void controllerAnnotationRegistration() {
        ApplicationContext context = new ApplicationContext("com.diy");

        Object controller = context.getBean("LectureController");

        assertThat(controller).isInstanceOf(LectureController.class);
    }
}
