package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private static final List<Lecture> STORAGE = new ArrayList<>();
    static {
        STORAGE.add(new Lecture(1L, "스프링 DIY", 30000L));
        STORAGE.add(new Lecture(2L, "리액트 입문", 25000L));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println(">>> doGet 호출됨! URL=" + req.getRequestURI());

        req.setAttribute("lectures", STORAGE);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        byte[] bodyBytes = req.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        System.out.println(">>> POST 받은 body: " + body);

        ObjectMapper mapper = new ObjectMapper();
        Lecture lecture = mapper.readValue(body, Lecture.class);

        long nextId = STORAGE.size() + 1;
        lecture.setId(nextId);
        STORAGE.add(lecture);

        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        byte[] bodyBytes = req.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        System.out.println(">>> PUT 받은 body: " + body);

        ObjectMapper mapper = new ObjectMapper();
        Lecture updated = mapper.readValue(body, Lecture.class);

        Lecture target = STORAGE.stream()
                .filter(l -> l.getId().equals(updated.getId()))
                .findFirst()
                .orElse(null);

        if (target == null) {
            resp.setStatus(404);
            return;
        }

        target.setName(updated.getName());
        target.setPrice(updated.getPrice());

        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(mapper.writeValueAsString(target));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idStr = req.getParameter("id");
        System.out.println(">>> DELETE 요청, id=" + idStr);

        if (idStr == null) {
            resp.setStatus(400);
            return;
        }

        Long id = Long.parseLong(idStr);
        boolean removed = STORAGE.removeIf(l -> l.getId().equals(id));

        if (!removed) {
            resp.setStatus(404);
            return;
        }

        resp.setStatus(204);
    }
}
