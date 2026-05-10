package com.diy.app;

import com.diy.framework.web.server.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class LectureController implements Controller {

    private static final ConcurrentHashMap<Long, Lecture> lectureMap = new ConcurrentHashMap<>();

    static {
        System.out.println("[LectureController] initialized.");
        final Lecture loopakBeL2 = Lecture.of(1L, "Loop:PAK BE L2", BigDecimal.valueOf(1300000));
        final Lecture springDiy = Lecture.of(2L, "Spring DIY", BigDecimal.valueOf(99000));
        lectureMap.put(1L, loopakBeL2);
        lectureMap.put(2L, springDiy);
    }

    @Override
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        switch (request.getMethod()) {
            case "GET" -> doGet(request, response);
            case "POST" -> doPost(request, response);
            case "PUT" -> doPut(request, response);
            case "DELETE" -> doDelete(request, response);
            default -> response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[LectureController] doGet() is called.");
        req.setAttribute("lectures", lectureMap.values());
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    private void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("[LectureController] doPost() is called.");
        final var lectureRequest = new ObjectMapper().readValue(readRequestBodyAsString(req), LecturePostRequest.class);
        final long lectureId = nextId();
        lectureMap.put(lectureId, lectureRequest.toLecture(lectureId));
        redirectToList(resp);
    }


    private void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("[LectureController] doPut() is called.");
        final var putRequest = new ObjectMapper().readValue(readRequestBodyAsString(req), LecturePutRequest.class);
        final var lectureId = putRequest.getId();
        final var target = Lecture.of(lectureId, putRequest.getName(), putRequest.getPrice());
        lectureMap.put(lectureId, target);
        redirectToList(resp);
    }


    private void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("[LectureController] doDelete() is called.");
        final Long lectureId = extractLectureId(req);
        lectureMap.remove(lectureId);
        redirectToList(resp);
    }

    @NotNull
    private String readRequestBodyAsString(final HttpServletRequest req) throws IOException {
        final var bytes = req.getInputStream().readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
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

    private static void redirectToList(final HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/lectures");
    }
}
