package com.diy.app;

import com.diy.framework.web.Controller;
import com.diy.framework.web.mvc.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LectureController implements Controller {
    private final LectureRepository lectureRepository = new LectureRepositoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String method = request.getMethod();

        return switch (method) {
            case "GET" -> doGet(request, response);
            case "POST" -> doPost(request, response);
            case "PUT" -> doPut(request, response);
            case "DELETE" -> doDelete(request, response);
            default -> throw new RuntimeException("404 Not Found");
        };
    }

    private ModelAndView doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Lecture lecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
        lectureRepository.delete(lecture);
        return new ModelAndView(null);
    }

    private ModelAndView doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Lecture lecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
        lectureRepository.update(lecture);
        return new ModelAndView(null);
    }

    private ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Lecture lecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
        lectureRepository.save(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = Map.of("lectures", lectureRepository.findAll());
        return new ModelAndView("lecture-list", model);
    }
    
    


}
