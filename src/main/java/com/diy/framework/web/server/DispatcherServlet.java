package com.diy.framework.web.server;

import com.diy.app.controller.*;
import com.diy.app.controller.lecture.LectureDeleteController;
import com.diy.app.controller.lecture.LectureGetController;
import com.diy.app.controller.lecture.LecturePostController;
import com.diy.app.controller.lecture.LecturePutController;
import com.diy.app.repository.InMemoryLectureRepositoryImpl;
import com.diy.app.repository.LectureRepository;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private HandlerMapping handlerMapping;
    private LectureRepository lectureRepository;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        lectureRepository = new InMemoryLectureRepositoryImpl();
        objectMapper = new ObjectMapper();
        this.handlerMapping = new HandlerMapping(
                Map.of(
                        new HandlerContext("/lectures", HttpMethod.GET), new LectureGetController(lectureRepository),
                        new HandlerContext("/lectures", HttpMethod.POST), new LecturePostController(lectureRepository, objectMapper),
                        new HandlerContext("/lectures", HttpMethod.PUT), new LecturePutController(lectureRepository, objectMapper),
                        new HandlerContext("/lectures", HttpMethod.DELETE), new LectureDeleteController(lectureRepository, objectMapper)
                )
        );
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        HttpMethod httpMethod = HttpMethod.valueOf(req.getMethod());
        String path = req.getRequestURI();
        HandlerContext requestContext = new HandlerContext(path, httpMethod);
        AbstractController controller = handlerMapping.getController(requestContext);
        try {
            controller.handleRequest(req, resp);
        } catch (Exception e){
            System.out.println("unexpected error");
        }
    }

    private Map<String, ?> getBody(final HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {
            });
        } else {
            return req.getParameterMap();
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final String viewName = mav.getViewName();

//        final View view = viewResolver.resolveViewName(viewName);

//        if (view == null) {
//            throw new RuntimeException("View not found: " + viewName);
//        }
//
//        view.render(mav.getModel(), req, resp);
    }
}
