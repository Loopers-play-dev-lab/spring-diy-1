package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Map<Long, Lecture> repository = new HashMap<>();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final Collection<Lecture> lectures = repository.values();

        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("lecture-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String body = new String(req.getInputStream().readAllBytes());
        final Lecture lecture = OBJECT_MAPPER.readValue(body, Lecture.class);

        final long id = repository.size() + 1L;
        lecture.setId(id);
        repository.put(lecture.getId(), lecture);

        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final Long id = Long.valueOf(req.getParameter("id"));
        repository.remove(id);

        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String body = new String(req.getInputStream().readAllBytes());
        final Lecture lecture = OBJECT_MAPPER.readValue(body, Lecture.class);

        repository.put(lecture.getId(), lecture);

        resp.sendRedirect("/lectures");
    }
}
