package com.diy.app;

import com.diy.framework.view.ModelAndView;
import com.diy.framework.web.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LectureController implements Controller {
    private final LectureRepository repository = new LectureRepositoryImpl();

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return switch (req.getMethod()) {
            case "GET" -> doGet(req, resp);
            case "POST" -> doPost(req, resp);
            case "PUT" -> doPut(req, resp);
            case "DELETE" -> doDelete(req, resp);
            default -> throw new Exception("404 Not Found");
        };
    }

    protected ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Long, Lecture> lectures = repository.findAll();
        Map<String, Object> model = Map.of("lectures", lectures.values());
        return new ModelAndView("lecture-list", model);
    }

    protected ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = parseJsonToLecture(req);
        repository.save(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    protected ModelAndView doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = parseJsonToLecture(req);
        repository.update(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    protected ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = parseJsonToLecture(req);
        repository.delete(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    private Lecture parseJsonToLecture(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        return(mapper.readValue(req.getInputStream(), Lecture.class));
    }
}
