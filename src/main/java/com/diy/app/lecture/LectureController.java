package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;
import com.diy.framework.Controller;
import com.diy.framework.ModelAndView;
import com.diy.framework.exception.MethodNotAllowedException;
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

    public LectureController(LectureService lectureService) {
        this.objectMapper = new ObjectMapper();
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equals("GET")) {
            return doGet(request, response);
        } else if (request.getMethod().equals("POST")) {
            return doPost(request, response);
        }

        throw new MethodNotAllowedException(request.getMethod());
    }

    private ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Lecture> lectures = lectureService.getAllLectures();

        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);

        return new ModelAndView("lecture-list", model);
    }

    private ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        CreateLectureRequest createRequest = objectMapper.readValue(body, CreateLectureRequest.class);

        lectureService.createLecture(createRequest);

        return new ModelAndView("redirect:/lectures");
    }
}
