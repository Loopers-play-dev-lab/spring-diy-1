package com.diy.app.servlet;

import com.diy.app.domain.Lecture;
import com.diy.app.service.LectureService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


@WebServlet("/lectures")
public class LecturesServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LectureService lectureService = new LectureService();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.registerLecture(lecture);
        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);

        try {
            lectureService.modifyLecture(lecture);
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DeleteRequest deleteRequest = objectMapper.readValue(req.getReader(), DeleteRequest.class);

        try {
            lectureService.deleteLecture(deleteRequest.getId());
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("lecture-list.jsp");
        Collection<Lecture> lectures = lectureService.getLectures().values();
        req.setAttribute("lectures", lectures);
        requestDispatcher.forward(req, resp);
    }

    static class DeleteRequest {
        String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}


