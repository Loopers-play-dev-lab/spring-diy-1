package com.diy.app.servlet;

import com.diy.app.domain.Lecture;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    long nextId;
    List<Lecture> lectures;

    @Override
    public void init() throws ServletException {
        nextId = 0;
        lectures = new ArrayList<>();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
        resp.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
        Lecture lecture = new Lecture(nextId++, body.getName(), body.getPrice());
        lectures.add(lecture);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(lecture));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
        lectures.stream().filter(lec -> lec.getId() == body.getId()).forEach(lec -> lec.update(body));

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(body));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        lectures.removeIf(lecture -> lecture.getId() == id);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
