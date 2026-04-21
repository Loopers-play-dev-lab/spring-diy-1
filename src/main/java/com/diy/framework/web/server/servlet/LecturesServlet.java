package com.diy.framework.web.server.servlet;

import com.diy.app.domain.Lecture;
import com.diy.app.infrastructure.LectureRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LecturesServlet extends HttpServlet {

    private final LectureRepository lectureRepository = new LectureRepository();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        List<Lecture> lectures = lectureRepository.getLectures();

        request.setAttribute("lectures", lectures);
        request.getRequestDispatcher("lecture-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String jsonData;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            jsonData = br.lines().collect(Collectors.joining());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Lecture lecture = objectMapper.readValue(jsonData, Lecture.class);

        lectureRepository.save(lecture);
    }

    @Override
    protected void doPut(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));

        String jsonData;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            jsonData = br.lines().collect(Collectors.joining());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Lecture updated = objectMapper.readValue(jsonData, Lecture.class);

        lectureRepository.update(id, updated);
    }

    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        lectureRepository.delete(id);
    }
}
