package com.diy.app;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public class LectureController implements Controller {

    private final LectureService lectureService;

    public LectureController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if ("POST".equals(request.getMethod())) {
            return doPost(request, response);
        } else if ("GET".equals(request.getMethod())) {
            return doGet(request, response);
        }

        throw new RuntimeException("404 Not Found");
    }

    private ModelAndView doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final byte[] bodyBytes = req.getInputStream().readAllBytes();
        final String body = new String(bodyBytes, StandardCharsets.UTF_8);

        final Lecture lecture = new ObjectMapper().readValue(body, Lecture.class);
        lectureService.save(lecture);

        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doGet(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final Collection<Lecture> lectures = lectureService.findAll();
        final Map<String, Object> model = new HashMap<>();
        final Object lectureModels = lectures.stream().map(lecture -> Map.of("id", lecture.getId(), "name", lecture.getName(), "price", lecture.getPrice())).toList();
        model.put("lectures", lectureModels);

        return new ModelAndView("lecture-list", model);
    }
}
