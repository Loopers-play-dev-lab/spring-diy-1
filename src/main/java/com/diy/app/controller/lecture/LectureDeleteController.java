package com.diy.app.controller.lecture;

import com.diy.app.controller.AbstractController;
import com.diy.app.repository.LectureRepository;
import com.diy.app.servlet.dto.request.LectureDelete;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureDeleteController implements AbstractController {
    private final LectureRepository lectureRepository;
    private final ObjectMapper objectMapper;

    public LectureDeleteController(LectureRepository lectureRepository, ObjectMapper objectMapper) {
        this.lectureRepository = lectureRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LectureDelete deleteRequest = objectMapper.readValue(request.getInputStream(), LectureDelete.class);
        lectureRepository.deleteById(deleteRequest.id());
        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        response.setHeader("Location", "/lectures");
    }
}
