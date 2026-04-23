package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;
import com.diy.framework.Controller;
import com.diy.framework.exception.MethodNotAllowedException;
import com.diy.framework.view.View;
import com.diy.framework.view.ViewResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureController implements Controller {

    private final ObjectMapper objectMapper;
    private final LectureService lectureService;
    private final ViewResolver viewResolver;

    public LectureController(LectureService lectureService, ViewResolver viewResolver) {
        this.objectMapper = new ObjectMapper();
        this.lectureService = lectureService;
        this.viewResolver = viewResolver;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equals("GET")) {
            doGet(request, response);
        } else if (request.getMethod().equals("POST")) {
            doPost(request, response);
        } else {
            throw new MethodNotAllowedException(request.getMethod());
        }
    }

    private void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Lecture> lectures = lectureService.getAllLectures();

        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);

        View view = viewResolver.resolveViewName("lecture-list");
        view.render(request, response, model);
    }

    private void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        CreateLectureRequest createRequest = objectMapper.readValue(body, CreateLectureRequest.class);

        lectureService.createLecture(createRequest);

        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        response.setHeader("Location", "/lectures");
    }
}
