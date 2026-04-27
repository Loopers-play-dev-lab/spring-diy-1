package com.diy.app;

import com.diy.framework.web.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureController implements Controller {
    private final LectureRepository lectureRepository = new LectureRepositoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        switch (method) {
            case "GET":
                request.setAttribute("lectures", lectureRepository.findAll());
                request.getRequestDispatcher("/lecture-list.jsp").forward(request, response);
                break;

            case "POST":
                Lecture lecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
                lectureRepository.save(lecture);
                response.sendRedirect("/lectures");
                break;

            case "PUT":
                Lecture putLecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
                lectureRepository.update(putLecture);
                break;

            case "DELETE":
                Lecture deleteLecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
                lectureRepository.delete(deleteLecture);
                break;
        }
    }
}
