package com.diy.app.controller;

import com.diy.app.domain.Lecture;
import com.diy.app.service.LectureService;
import com.diy.app.servlet.ModelAndView;
import com.diy.framework.web.annotation.Autowired;
import com.diy.framework.web.annotation.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class LectureController implements Controller {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private LectureService lectureService;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        switch (request.getMethod()) {
            case "GET":
                return doGet(request, response);
            case "POST":
                return doPost(request, response);
            case "PUT":
                doPut(request, response);
                return null;
            case "DELETE":
                doDelete(request, response);
                return null;
        }

        throw new RuntimeException("404 Not Found");
    }

    private void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DeleteRequest deleteRequest = objectMapper.readValue(request.getReader(), DeleteRequest.class);

        try {
            lectureService.deleteLecture(deleteRequest.getId());
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(e.getMessage());
        }
    }

    private void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Lecture lecture = objectMapper.readValue(request.getReader(), Lecture.class);

        try {
            lectureService.modifyLecture(lecture);
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(e.getMessage());
        }
    }

    private ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Lecture lecture = objectMapper.readValue(request.getReader(), Lecture.class);
        lectureService.registerLecture(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Collection<Lecture> lectures = lectureService.getLectures().values();
        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);
        return new ModelAndView("lecture-list", model);
    }

    static class DeleteRequest {
        String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


}
