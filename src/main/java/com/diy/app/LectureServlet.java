package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    private static final List<Lecture> lectures = new ArrayList<>();

    static {
        lectures.add(new Lecture(1L, "루퍼스", "엄청난 강의입니다."));
        lectures.add(new Lecture(2L, "Spring Framework", "스프링 프레임워크 강의입니다."));
        lectures.add(new Lecture(3L, "jude diy class", "jude diy class입니다."));
    }

    private static long sequence = lectures.size();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = new ObjectMapper().writeValueAsString(lectures);

        resp.setStatus(200); // OK
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] bodyBytes = req.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        if (body.isEmpty()) {
            resp.setStatus(400); // Bad Request
            return;
        }

        Lecture lecture = new ObjectMapper().readValue(body, Lecture.class);
        lecture.id = ++sequence;
        lectures.add(lecture);

        resp.setStatus(201); // Created
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = new ObjectMapper().writeValueAsString(lecture);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] bodyBytes = req.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        if (body.isEmpty()) {
            resp.setStatus(400); // Bad Request
            return;
        }

        Lecture lecture = new ObjectMapper().readValue(body, Lecture.class);
        for (int i = 0; i < lectures.size(); i++) {
            if (lectures.get(i).id.equals(lecture.id)) {
                lectures.set(i, lecture);
                resp.setStatus(200); // OK
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                String json = new ObjectMapper().writeValueAsString(lecture);
                resp.getWriter().write(json);
                return;
            }
        }

        resp.setStatus(404); // Not Found
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(400); // Bad Request
            return;
        }

        Long id = Long.parseLong(idParam);
        for (int i = 0; i < lectures.size(); i++) {
            if (lectures.get(i).id.equals(id)) {
                lectures.remove(i);
                resp.setStatus(204); // No Content
                return;
            }
        }

        resp.setStatus(404); // Not Found
    }
}
