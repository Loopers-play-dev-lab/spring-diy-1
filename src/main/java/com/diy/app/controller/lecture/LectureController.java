package com.diy.app.controller.lecture;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LectureRepository;
import com.diy.app.servlet.dto.request.LectureDelete;
import com.diy.app.servlet.dto.request.LecturePost;
import com.diy.app.servlet.dto.request.LectureUpdate;
import com.diy.app.servlet.dto.response.LectureResponse;
import com.diy.framework.web.controller.AbstractController;
import com.diy.framework.web.server.HttpMethod;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureController implements AbstractController {

    private final ObjectMapper objectMapper;
    private final LectureRepository lectureRepository;

    public LectureController(ObjectMapper objectMapper, LectureRepository lectureRepository) {
        this.objectMapper = objectMapper;
        this.lectureRepository = lectureRepository;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        switch (HttpMethod.valueOf(request.getMethod())) {
            case GET -> {
                return doGet(request, response);
            }
            case PUT -> {
                return doPut(request, response);
            }
            case POST -> {
                return doPost(request, response);
            }
            case DELETE -> {
                return doDelete(request, response);
            }
        }

        throw new IllegalArgumentException("no method match");
    }

    private ModelAndView doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LecturePost postRequest = objectMapper.readValue(req.getInputStream(), LecturePost.class);
        Lecture lecture = Lecture.makeForSave(postRequest);
        lectureRepository.save(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doGet(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        List<LectureResponse> lectures = lectureRepository.find().stream().map(LectureResponse::from).toList();
        Map<String, Object> map = new HashMap<>();
        map.put("lectures", lectures);
        return new ModelAndView("lecture-list.jsp", map);
    }

    private ModelAndView doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LectureDelete deleteRequest = objectMapper.readValue(req.getInputStream(), LectureDelete.class);
        lectureRepository.deleteById(deleteRequest.id());
        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LectureUpdate updateRequest = objectMapper.readValue(req.getInputStream(), LectureUpdate.class);
        Lecture updateLecture = new Lecture(updateRequest.id(), updateRequest.name(), updateRequest.price());
        lectureRepository.updateById(updateRequest.id(), updateLecture);
        return new ModelAndView("redirect:/lectures");
    }
}
