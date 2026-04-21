package com.diy.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    private final Map<Long, Lecture> lectureRepository = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    final String REDIRECT_PATH = "/lectures";
    final String ATTRIBUTE_KEY = "lectures";
    final String DISPATCHER = "/lecture-list.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(ATTRIBUTE_KEY, lectureRepository.values());
        req.getRequestDispatcher(DISPATCHER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonNode body = objectMapper.readTree(req.getInputStream());

        long id = autoIncrement();
        String name = body.get("name").asText();
        double price = body.get("price").asDouble();

        lectureRepository.put(id, new Lecture(id, name, price));

        resp.sendRedirect(REDIRECT_PATH);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        lectureRepository.remove(Long.parseLong(id));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Optional.ofNullable(lectureRepository.get(id))
                .orElseThrow(() -> {
                    resp.setStatus(404);
                    return new ServletException("Lecture with id " + id + " not found");
                });
        JsonNode body = objectMapper.readTree(req.getInputStream());

        String name = body.get("name").asText();
        double price = body.get("price").asDouble();

        lectureRepository.put(id, new Lecture(id, name, price));
    }

    private long autoIncrement() {
        return lectureRepository.size() + 1L;
    }
}
