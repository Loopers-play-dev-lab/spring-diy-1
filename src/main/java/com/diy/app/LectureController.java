package com.diy.app;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LectureController implements Controller {

    private final LectureRepository lectureRepository = new LectureRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if ("GET".equals(request.getMethod())) {
            return doGet(request, response);
        } else if ("POST".equals(request.getMethod())) {
            return doPost(request, response);
        }

        throw new RuntimeException("404 Not Found");
    }

    private ModelAndView doGet(final HttpServletRequest req, final HttpServletResponse resp) {
        final Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectureRepository.findAll());
        return new ModelAndView("lectures-list", model);
    }

    private ModelAndView doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        lectureRepository.save(lecture);
        return new ModelAndView("redirect:/lectures");
    }
}
