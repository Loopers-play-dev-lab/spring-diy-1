package com.diy.app.lecture;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LectureService lectureService = new LectureService();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Lecture lecture = objectMapper.readValue(
                req.getInputStream(),
                Lecture.class
        );
        lectureService.save(lecture);
        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("lecture-list.jsp");
        List<Lecture> lectures = lectureService.getLectures();
        req.setAttribute("lectures", lectures);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            Lecture lecture = objectMapper.readValue(
                    req.getInputStream(),
                    Lecture.class
            );
            lectureService.update(lecture);
        } catch (IllegalArgumentException e) {
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Lecture lecture = objectMapper.readValue(
                req.getInputStream(),
                Lecture.class
        );
        lectureService.delete(lecture);
    }
}
