package com.diy.app.controller;

import com.diy.app.model.Lecture;
import com.diy.app.service.LectureService;
import com.diy.framework.web.anotation.Autowired;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LectureController implements Controller {
    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equals("GET")) {
            return doGet();
        } else if (request.getMethod().equals("POST")) {
            return doPost(request);
        }

        throw new RuntimeException("404 Not Found");
    }

    public ModelAndView doPost(final HttpServletRequest req) throws IOException {
        final String body = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        final Lecture lecture = new ObjectMapper().readValue(body, Lecture.class);

        lectureService.register(lecture);

        return new ModelAndView("redirect:/lectures/v1");
    }

    public ModelAndView doGet() {
        final Collection<Lecture> lectures = lectureService.getLectures();
        final Map<String, Object> model = new HashMap<>();
        final Object lectureModels = lectures.stream().map(lecture -> Map.of("id", lecture.getId(), "name", lecture.getName(), "price", lecture.getPrice())).toList();
        model.put("lectures", lectureModels);

        return new ModelAndView("lecture-list", model);
    }
}
