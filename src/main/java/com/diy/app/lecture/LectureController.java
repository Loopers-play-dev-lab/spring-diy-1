package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;
import com.diy.framework.Controller;
import com.diy.framework.exception.MethodNotAllowedException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LectureController implements Controller {

    private final ObjectMapper objectMapper;
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.objectMapper = new ObjectMapper();
        this.lectureService = lectureService;
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

        request.setAttribute("lectures", lectures);

        String viewPath = "lecture-list.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
        requestDispatcher.forward(request, response);
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
