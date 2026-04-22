package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private final LectureRepository repository = new LectureRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("lectures", repository.findAll());
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        repository.save(lecture);
        res.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        repository.update(lecture);
        res.sendRedirect("/lectures");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        repository.deleteById(id);
        res.sendRedirect("/lectures");
    }
}
