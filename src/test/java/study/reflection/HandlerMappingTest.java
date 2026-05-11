package study.reflection;

import static org.junit.jupiter.api.Assertions.*;

import com.diy.app.Main;
import com.diy.app.utils.ApplicationContext;
import com.diy.app.utils.HandlerMapping;
import com.diy.app.utils.HandlerExecution;
import com.diy.app.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class HandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        // 1. 프레임워크 엔진 기동 (빈 스캔 및 의존성 주입)
        ApplicationContext.run(Main.class);

        // 2. 초기화된 HandlerMapping 빈 가져오기
        handlerMapping = ApplicationContext.getBean(HandlerMapping.class);

        // 3. @Controller 스캔 및 매핑 지도 생성
        handlerMapping.initalize();
    }

    @Test
    @DisplayName("강의 조회 테스트 - GET /lectures")
    void GetMappingTest() throws Exception {
        // Given
        String method = "GET";
        String uri = "/lectures";

        // When
        HandlerExecution execution = handlerMapping.getHandler(method, uri);

        // Then
        assertNotNull(execution, "GET:/lectures에 매핑된 핸들러가 있어야 합니다.");

        // 실행 확인 (req, resp는 인터페이스이므로 실제 테스트시에는 가짜 구현체나 null 전달)
        ModelAndView mav = execution.handle(null, null);
        assertNotNull(mav);
        System.out.println("View Name: " + mav.getViewName());
    }

    @Test
    @DisplayName("강의 등록 테스트 - POST /lectures")
    void PostMappingTest() throws Exception {
        // Given
        String method = "POST";
        String uri = "/lectures";

        // When
        HandlerExecution execution = handlerMapping.getHandler(method, uri);

        // Then
        assertNotNull(execution, "POST:/lectures에 매핑된 핸들러가 있어야 합니다.");

        // 리플렉션으로 찾은 메서드가 실제 PostController의 메서드인지 확인 가능
        System.out.println("Mapped Method: " + execution.toString());
    }

    @Test
    @DisplayName("강의 수정 테스트 - PUT /lectures")
    void PutMappingTest() throws Exception {
        // Given
        String method = "PUT";
        String uri = "/lectures";

        // When
        HandlerExecution execution = handlerMapping.getHandler(method, uri);

        // Then
        assertNotNull(execution, "PUT:/lectures 매핑 확인");
    }

    @Test
    @DisplayName("강의 삭제 테스트 - DELETE /lectures")
    void DeleteMappingTest() throws Exception {
        // Given
        String method = "DELETE";
        String uri = "/lectures";

        // When
        HandlerExecution execution = handlerMapping.getHandler(method, uri);

        // Then
        assertNotNull(execution, "DELETE:/lectures 매핑 확인");
    }
}