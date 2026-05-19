package com.diy.app.controller;

import com.diy.app.controller.dto.OpenLectureRequest;
import com.diy.app.domain.Lecture;
import com.diy.app.domain.Price;
import com.diy.app.service.LectureService;
import com.diy.framework.web.HttpRequestMethod;
import com.diy.framework.web.ModelAndView;
import com.diy.framework.web.beans.factory.annotation.Autowired;
import com.diy.framework.web.beans.factory.annotation.Component;
import com.diy.framework.web.controller.Controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.diy.app.controller.dto.LectureListResponse.*;

@Component
public class LectureController implements Controller {

    private final LectureService lectureService;
    private final ObjectMapper objectMapper;

    public @Autowired LectureController(final LectureService lectureService) {
        this.lectureService = lectureService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // TODO: 객체지향적으로 변경해보기
        switch (HttpRequestMethod.from(req.getMethod())) {
            case POST -> {
                return doPost(req, res);
            }
            case GET -> {
                return doGet(req, res);
            }
            default -> {
                throw new RuntimeException("405 Method Not Allowed");
            }
        }

    }

    private ModelAndView doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Collection<Lecture> lectures = lectureService.getLecture();
        req.setAttribute("lectures", lectures);
        Map<String, List<LectureListDto>> model = parseParams(req);

        return new ModelAndView("lecture-list", model);
    }

    private ModelAndView doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final String body = new String(req.getInputStream().readAllBytes());
        final OpenLectureRequest openLectureRequest = objectMapper.readValue(body, OpenLectureRequest.class);

        lectureService.registerLecture(Lecture.open(
                openLectureRequest.name(),
                Price.of(openLectureRequest.price())
        ));

        return new ModelAndView("redirect:/lectures");
    }

    private <T> Map<String, T> parseParams(final HttpServletRequest req) throws IOException {
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
