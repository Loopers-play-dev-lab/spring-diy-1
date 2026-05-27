package com.diy.framework.web.beans.factory;

import com.diy.app.LectureController;
import com.diy.app.LectureRepository;
import com.diy.framework.web.context.ApplicationContext;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.handler.AnnotationHandlerMapping;
import com.diy.framework.web.mvc.handler.HandlerMethod;
import com.diy.framework.web.mvc.handler.SimpleControllerHandlerMapping;
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

    @Test
    @DisplayName("인터페이스 기반 컨트롤러는 빈 이름(URL)으로 등록되고 HTTP Method 와 무관하게 매핑된다")
    void interfaceControllerMapping() {
        ApplicationContext context = new ApplicationContext("com.diy");

        Object controller = context.getBean("/lectures/new");
        assertThat(controller).isInstanceOf(Controller.class);

        SimpleControllerHandlerMapping mapping = new SimpleControllerHandlerMapping(context);
        mapping.initialize();

        assertThat(mapping.getHandler("GET /lectures/new")).isSameAs(controller);
        assertThat(mapping.getHandler("POST /lectures/new")).isSameAs(controller);
    }

    @Test
    @DisplayName("애너테이션 기반 컨트롤러는 URL과 HTTP Method 조합으로 매핑된다")
    void annotationControllerMapping() {
        ApplicationContext context = new ApplicationContext("com.diy");

        AnnotationHandlerMapping mapping = new AnnotationHandlerMapping(context);
        mapping.initialize();

        Object getHandler = mapping.getHandler("GET /lectures");
        Object postHandler = mapping.getHandler("POST /lectures");

        assertThat(getHandler).isInstanceOf(HandlerMethod.class);
        assertThat(postHandler).isInstanceOf(HandlerMethod.class);

        assertThat(((HandlerMethod) getHandler).getMethod().getName()).isEqualTo("list");
        assertThat(((HandlerMethod) postHandler).getMethod().getName()).isEqualTo("create");
    }
}
