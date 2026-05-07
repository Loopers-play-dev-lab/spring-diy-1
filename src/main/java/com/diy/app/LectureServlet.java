package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private static final ConcurrentHashMap<Long, Lecture> lectureMap = new ConcurrentHashMap<>();

    @Override
    public void init() throws ServletException {
        System.out.println("[LectureServlet] init() is called.");
        super.init();
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[LectureServlet] service() is called.");
        super.service(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[LectureServlet] doGet() is called.");
        req.setAttribute("lectures", lectureMap.values());
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("[LectureServlet] doPost() is called.");
        final var reader = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
        final var mapper = new ObjectMapper();
        final var lectureRequest = mapper.readValue(reader.readLine(), LectureRequest.class);

        final long lectureId = nextId();
        lectureMap.put(lectureId, lectureRequest.toLecture(lectureId));
        resp.sendRedirect("/lectures");
    }

    @Override
    public void destroy() {
        System.out.println("[LectureServlet] destroy() is called.");
        super.destroy();
    }

    private long nextId() {
        return lectureMap.keySet().stream().mapToLong(Long::longValue).max().orElse(0L) + 1;
    }
}

