package com.diy.app;

import com.diy.framework.web.Controller;
import com.diy.framework.web.mvc.Model;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureController implements Controller {
    private final LectureRepository lectureRepository = new LectureRepositoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handleRequest(final HttpServletRequest request, final HttpServletResponse response, final Model model) throws Exception {
        String method = request.getMethod();
        Lecture lecture;

        switch (method) {
            case "GET":
                model.addAttribute("lectures", lectureRepository.findAll());
                return "lecture-list";

            case "POST":
                lecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
                lectureRepository.save(lecture);
                response.sendRedirect("/lectures");
                return null;

            case "PUT":
                lecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
                lectureRepository.update(lecture);
                return null;

            case "DELETE":
                lecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
                lectureRepository.delete(lecture);
                return null;
        }
        return null;
    }
}
