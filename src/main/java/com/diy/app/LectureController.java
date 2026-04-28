package com.diy.app;

import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.JspView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class LectureController implements Controller {
    private static final Map<Integer, Lecture> lectures = new LinkedHashMap<>();

    public void handleRequest(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        String method = req.getMethod();
        switch (method) {
            case "GET":
                doGet(req, resp);
                break;
            case "POST":
                doPost(req, resp);
                break;
            case "PUT":
                doPut(req, resp);
                break;
            case "DELETE":
                doDelete(req, resp);
                break;
        }
    }

    public void doGet(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("lectures", lectures.values());
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    public void doPost(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> body = objectMapper.readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {});
            Lecture lecture = Lecture.register(body.get("name"), new BigDecimal(body.get("price")));
            lectures.put(lecture.getId(), lecture);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
        }
    }

    public void doPut(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> body = objectMapper.readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {});

            int id = Integer.parseInt(body.get("id"));
            Lecture lecture = lectures.get(id);
            if (lecture != null) {
                lecture.updateName(body.get("name"));
                lecture.updatePrice(new BigDecimal(body.get("price")));
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
        }
    }

    public void doDelete(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> body = objectMapper.readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {
            });

            int id = Integer.parseInt(body.get("id"));
            lectures.remove(id);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
        }
    }
}