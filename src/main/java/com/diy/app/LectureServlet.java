package com.diy.app;

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

    private final LectureRepository lectureRepository = new LectureRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp ) throws ServletException, IOException {
        final Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        lectureRepository.save(lecture);

        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp ) throws ServletException, IOException {
        final List<Lecture> lectures = lectureRepository.findAll();

        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);

        lectureRepository.update(lecture);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final Long id = Long.parseLong(req.getParameter("id"));

        lectureRepository.delete(id);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
