package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    private static final Map<Integer, Lecture> lectures = new LinkedHashMap<>();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doGet(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("lectures", lectures.values());
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    @Override
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

    @Override
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

    @Override
    public void doDelete(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> body = objectMapper.readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {});

            int id = Integer.parseInt(body.get("id"));
            lectures.remove(id);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
        }
    }
}
