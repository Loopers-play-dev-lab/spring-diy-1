package com.diy.app;

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
    private List<Lecture> lectures  = new ArrayList<>();

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        resp.getWriter().write(objectMapper.writeValueAsString(lectures));
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        lectures.add(lecture);

        resp.setStatus(HttpServletResponse.SC_CREATED); // 201
        resp.getWriter().write(objectMapper.writeValueAsString(lecture));
    }

    @Override
    public void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        Lecture updated = objectMapper.readValue(req.getInputStream(), Lecture.class);

        for (int i = 0; i < lectures.size(); i++) {
            if (lectures.get(i).id().equals(updated.id())) {
                lectures.set(i, updated);
                resp.getWriter().write(objectMapper.writeValueAsString(updated));
                return;
            }
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
    }

    @Override
    public void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        Integer id = objectMapper.readTree(req.getInputStream()).get("id").asInt();

        boolean removed = lectures.removeIf(lecture -> lecture.id().equals(id));

        if (removed) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
        }
    }
}
