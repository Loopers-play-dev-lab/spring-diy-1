package com.diy.app.controller.lecture;

import com.diy.app.controller.AbstractController;
import com.diy.app.domain.Lecture;
import com.diy.app.repository.LectureRepository;
import com.diy.app.servlet.dto.request.LecturePost;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LecturePostController implements AbstractController {
    private final LectureRepository lectureRepository;
    private final ObjectMapper objectMapper;

    public LecturePostController(LectureRepository lectureRepository, ObjectMapper objectMapper) {
        this.lectureRepository = lectureRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LecturePost postRequest = objectMapper.readValue(request.getInputStream(), LecturePost.class);
        Lecture lecture = Lecture.makeForSave(postRequest);
        lectureRepository.save(lecture);
        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        response.setHeader("Location", "/lectures");
    }
}
