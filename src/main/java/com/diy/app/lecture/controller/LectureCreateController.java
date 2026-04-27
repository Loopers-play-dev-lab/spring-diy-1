package com.diy.app.lecture.controller;

import com.diy.app.lecture.Lecture;
import com.diy.app.lecture.LectureStorage;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class LectureCreateController implements Controller {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        Lecture lecture = mapper.readValue(body, Lecture.class);
        long nextId = LectureStorage.LECTURES.size() + 1;
        lecture.setId(nextId);
        LectureStorage.LECTURES.add(lecture);

        return new ModelAndView("redirect:/lectures");
    }
}
