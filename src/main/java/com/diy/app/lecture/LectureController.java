package com.diy.app.lecture;

import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.model.Model;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LectureController implements Controller{
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final LectureRepository lectureRepository = new LectureRepository();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();

        return switch (method) {
            case "GET" -> doGet(request, response);
            case "POST" -> doPost(request, response);
            case "PUT" -> doPut(request, response);
            case "DELETE" -> doDelete(request, response);
            default -> throw new RuntimeException("지원하지 않는 HTTP 메서드입니다");
        };
    }

    protected ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) {
        Model model = new Model();
        model.addAttribute("lectures", lectureRepository.findAll());
        return new ModelAndView("lecture-list", model);
    }

    protected ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = OBJECT_MAPPER.readValue(req.getInputStream(), Lecture.class);
        lectureRepository.insert(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    protected ModelAndView doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = OBJECT_MAPPER.readValue(req.getInputStream(), Lecture.class);
        lectureRepository.update(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    protected ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp) {
        final Long id = Long.valueOf(req.getParameter("id"));
        lectureRepository.delete(id);
        return new ModelAndView("redirect:/lectures");
    }
}
