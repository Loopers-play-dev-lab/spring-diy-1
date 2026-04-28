package com.diy.app.controller;

import com.diy.app.model.Lecture;
import com.diy.app.view.JspView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LectureController implements Controller {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Map<Long, Lecture> repository = new HashMap<>();

    public void getLecture(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final Collection<Lecture> lectures = repository.values();
        req.setAttribute("lectures", lectures);
        JspView jspView = new JspView("lecture-list.jsp");
        jspView.render(req, res);
    }

    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String body = new String(req.getInputStream().readAllBytes());
        final Lecture lecture = OBJECT_MAPPER.readValue(body, Lecture.class);

        final long id = repository.size() + 1L;
        lecture.setId(id);
        repository.put(lecture.getId(), lecture);

        resp.sendRedirect("/lectures");
    }

    public void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final Long id = Long.valueOf(req.getParameter("id"));
        repository.remove(id);

        resp.sendRedirect("/lectures");
    }

    public void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String body = new String(req.getInputStream().readAllBytes());
        final Lecture lecture = OBJECT_MAPPER.readValue(body, Lecture.class);

        repository.put(lecture.getId(), lecture);

        resp.sendRedirect("/lectures");
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equals("GET")) {
            getLecture(request, response);
        } else if (request.getMethod().equals("POST")) {
            doPost(request, response);
        } else if (request.getMethod().equals("PUT")) {
            doPut(request, response);
        } else if (request.getMethod().equals("DELETE")) {
            doDelete(request, response);
        }
    }
}
