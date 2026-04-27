package com.diy.app.controller.lecture;

import com.diy.app.controller.AbstractController;
import com.diy.app.domain.Lecture;
import com.diy.app.repository.LectureRepository;
import com.diy.app.servlet.dto.request.LectureUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LecturePutController implements AbstractController {
    private final LectureRepository lectureRepository;
    private final ObjectMapper objectMapper;

    public LecturePutController(LectureRepository lectureRepository, ObjectMapper objectMapper) {
        this.lectureRepository = lectureRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LectureUpdate updateRequest = objectMapper.readValue(request.getInputStream(), LectureUpdate.class);
        Lecture updateLecture = new Lecture(updateRequest.id(), updateRequest.name(), updateRequest.price());
        lectureRepository.updateById(updateRequest.id(), updateLecture);
        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        response.setHeader("Location", "/lectures");
    }
}
