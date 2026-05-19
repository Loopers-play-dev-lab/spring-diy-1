package com.diy.app;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class LectureController implements Controller {

    private final LectureRepository lectureRepository = new LectureRepository();

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
        return new ModelAndView("lecture-list", Map.of("lectures", lectureRepository.findAll()));
    }

    private ModelAndView doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("[LectureController] doPost() is called.");
        final var lectureRequest = new ObjectMapper().readValue(readRequestBodyAsString(req), LecturePostRequest.class);
        lectureRepository.save(lectureRequest.toNewLecture());
        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doPut(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("[LectureController] doPut() is called.");
        final var putRequest = new ObjectMapper().readValue(readRequestBodyAsString(req), LecturePutRequest.class);
        existsLecture(putRequest.getId());
        lectureRepository.save(putRequest.toLecture());
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

    private void existsLecture(final Long lectureId) {
        if (!lectureRepository.exists(lectureId)) {
            throw new IllegalArgumentException("Lecture does not exist.");
        }
    }
}
