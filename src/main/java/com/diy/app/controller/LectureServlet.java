package com.diy.app.controller;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LectureRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private final LectureRepository lectureRepository = new LectureRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Lecture> lectures = lectureRepository.findByLecture();
        request.setAttribute("lectures", lectures);
        request.getRequestDispatcher("/lecture-list.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Lecture lecture = objectMapper.readValue(request.getInputStream(), Lecture.class);
        lectureRepository.register(lecture);

        response.sendRedirect("/lectures");
    }

}
