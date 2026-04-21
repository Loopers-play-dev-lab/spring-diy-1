package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private final LectureDao lectureDao = new LectureDao();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final List<Lecture> lectures = lectureDao.findAll();
        request.setAttribute("lectures", lectures);
        request.getRequestDispatcher("/lecture-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Lecture body = objectMapper.readValue(request.getInputStream(), Lecture.class);
        lectureDao.save(body);
        response.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Lecture body = objectMapper.readValue(request.getInputStream(), Lecture.class);
        try {
            lectureDao.update(body);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        lectureDao.deleteById(Long.parseLong(idParam));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
