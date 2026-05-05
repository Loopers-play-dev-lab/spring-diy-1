package com.diy.app.controller;

import com.diy.app.model.Lecture;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LectureController implements Controller {
    private final Map<Long, Lecture> lectureRepository = new HashMap<>();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equals("GET")) {
            return doGet(request, response);
        } else if (request.getMethod().equals("POST")) {
            return doPost(request, response);
        }

        throw new RuntimeException("404 Not Found");
    }

    public ModelAndView doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String body = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        final Lecture lecture = new ObjectMapper().readValue(body, Lecture.class);

        final long id = lectureRepository.size();
        lectureRepository.put(id, lecture);
        lecture.setId(id);

        return new ModelAndView("redirect:/lectures");
    }

    public ModelAndView doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final Collection<Lecture> lectures = lectureRepository.values();
        final Map<String, Object> model = new HashMap<>();
        final Object lectureModels = lectures.stream().map(lecture -> Map.of("id", lecture.getId(), "name", lecture.getName(), "price", lecture.getPrice())).toList();
        model.put("lectures", lectureModels);

        return new ModelAndView("lecture-list", model);
    }

}
