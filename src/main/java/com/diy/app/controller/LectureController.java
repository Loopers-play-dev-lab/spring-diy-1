package com.diy.app.controller;

import com.diy.app.controller.dto.LectureListResponse;
import com.diy.app.domain.Lecture;
import com.diy.app.domain.Price;
import com.diy.app.repository.LectureRepository;
import com.diy.framework.web.ModelAndView;
import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.view.JspView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.diy.app.controller.dto.LectureListResponse.*;

public class LectureController implements Controller {
    private final LectureRepository lectureRepository;

    public LectureController(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {

        switch (req.getMethod()) {
            case "POST" -> {
                return doPost(req, res);
            }
            case "GET" -> {
                return doGet(req, res);
            }
            default -> {
                throw new RuntimeException("404 Not Found");
            }
        }

    }

    private ModelAndView doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Collection<Lecture> lectures = lectureRepository.findAll();
        req.setAttribute("lectures", lectures);
        Map<String, List<LectureListDto>> model = parseParams(req);

        return new ModelAndView("lecture-list", model);
    }

    private ModelAndView doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final Map<String, String> map = parseParams(req);

        lectureRepository.save(Lecture.open(
                map.get("name"),
                Price.of(Long.parseLong(map.get("price")))
        ));

        return new ModelAndView("redirect:/lectures");
    }

    private <T> Map<String, T> parseParams(final HttpServletRequest req) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return objectMapper.readValue(body, new TypeReference<>() {});
        } else {
            String jsonStr = objectMapper.writeValueAsString(req.getParameterMap());
            return objectMapper.readValue(jsonStr, new TypeReference<>() {});
        }
    }
}
