package com.diy.app;

import com.diy.framework.web.server.Controller;
import com.diy.framework.web.server.mv.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LectureController implements Controller {

    private static final ConcurrentHashMap<Long, Lecture> lectureRepository = new ConcurrentHashMap<>();

    static {
        System.out.println("[LectureController] initialized.");
        final Lecture loopakBeL2 = Lecture.of(1L, "Loop:PAK BE L2", BigDecimal.valueOf(1300000));
        final Lecture springDiy = Lecture.of(2L, "Spring DIY", BigDecimal.valueOf(99000));
        lectureRepository.put(1L, loopakBeL2);
        lectureRepository.put(2L, springDiy);
    }

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        switch (request.getMethod()) {
            case "GET" -> {
                return doGet(request, response);
            }
            case "POST" -> {
                return doPost(request, response);
            }
            case "PUT" -> {
                return doPut(request, response);
            }
            case "DELETE" -> {
                return doDelete(request, response);
            }
            default -> {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                throw new RuntimeException("404 Not Found");
            }
        }
    }

    private ModelAndView doGet(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        System.out.println("[LectureController] doGet() is called.");
        return new ModelAndView("lecture-list", Map.of("lectures", lectureRepository.values()));
    }

    private ModelAndView doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("[LectureController] doPost() is called.");
        final var lectureRequest = new ObjectMapper().readValue(readRequestBodyAsString(req), LecturePostRequest.class);
        final long lectureId = nextId();
        lectureRepository.put(lectureId, lectureRequest.toLecture(lectureId));
        return new ModelAndView("redirect:/lectures");
    }


    private ModelAndView doPut(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("[LectureController] doPut() is called.");
        final var putRequest = new ObjectMapper().readValue(readRequestBodyAsString(req), LecturePutRequest.class);
        final var lectureId = putRequest.getId();
        final var target = Lecture.of(lectureId, putRequest.getName(), putRequest.getPrice());
        lectureRepository.put(lectureId, target);
        return new ModelAndView("redirect:/lectures");
    }


    private ModelAndView doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("[LectureController] doDelete() is called.");
        final Long lectureId = extractLectureId(req);
        lectureRepository.remove(lectureId);
        return new ModelAndView("redirect:/lectures");
    }

    @NotNull
    private String readRequestBodyAsString(final HttpServletRequest req) throws IOException {
        final var bytes = req.getInputStream().readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private long nextId() {
        return lectureRepository.keySet().stream().mapToLong(Long::longValue).max().orElse(0L) + 1;
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
