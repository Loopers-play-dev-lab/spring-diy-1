package com.diy.app;

import com.diy.framework.context.RequestMethod;
import com.diy.framework.context.annotation.Component;
import com.diy.framework.context.annotation.RequestMapping;
import com.diy.framework.web.mvc.IController;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequestMapping(value = "/test")
public class TestController implements IController {

    private final LectureService lectureService;

    public TestController(final LectureService lectureService) {
        this.lectureService = lectureService;
        System.out.println("testController::lectureService = " + lectureService);
    }

    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if ("POST".equals(request.getMethod())) {
            return doPost(request, response);
        } else if ("GET".equals(request.getMethod())) {
            return doGet(request, response);
        }

        throw new RuntimeException("404 Not Found");
    }

    public ModelAndView doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final byte[] bodyBytes = req.getInputStream().readAllBytes();
        final String body = new String(bodyBytes, StandardCharsets.UTF_8);

        final Lecture lecture = new ObjectMapper().readValue(body, Lecture.class);
        lectureService.registerLecture(lecture);

        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(methods = RequestMethod.GET, value = "/test")
    public ModelAndView doGet(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectureService.getLectures());

        return new ModelAndView("lecture-list", model);
    }
}
