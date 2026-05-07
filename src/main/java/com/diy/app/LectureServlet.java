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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private static final ConcurrentHashMap<Long, Lecture> lectureMap = new ConcurrentHashMap<>();

    static {
        final Lecture loopakBeL2 = Lecture.of(1L, "Loop:PAK BE L2", BigDecimal.valueOf(1300000));
        final Lecture springDiy = Lecture.of(2L, "Spring DIY", BigDecimal.valueOf(99000));
        lectureMap.put(1L, loopakBeL2);
        lectureMap.put(2L, springDiy);
    }

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
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final var reader = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
        final var putRequest = new ObjectMapper().readValue(reader.readLine(), LecturePutRequest.class);

        final var lectureId = extractLectureId(putRequest);
        final var target = Lecture.of(lectureId, putRequest.getName(), putRequest.getPrice());
        lectureMap.put(lectureId, target);

        resp.sendRedirect("/lectures");
    }

    private static Long extractLectureId(final LecturePutRequest putRequest) {
        final var lectureId = putRequest.getId();
        if (!lectureMap.containsKey(lectureId)) {
            throw new IllegalArgumentException("Invalid Lecture Information.");
        }
        return lectureId;
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[LectureServlet] doDelete() is called.");
        final Long lectureId = extractLectureId(req);
        lectureMap.remove(lectureId);
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

    private Long extractLectureId(final HttpServletRequest req) {
        final String lectureIdParam = req.getParameter("id");
        if (lectureIdParam == null || lectureIdParam.isBlank()) {
            throw new IllegalArgumentException("Lecture id is required.");
        }

        try {
            return Long.valueOf(lectureIdParam);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid lecture id.");
        }
    }
}

