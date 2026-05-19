package com.diy.app.lecture.controller;

import com.diy.app.lecture.entity.Lecture;
import com.diy.app.lecture.service.LectureService;
import com.diy.framework.web.annotation.Controller;
import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.annotation.RequestMethod;
import com.diy.framework.web.beans.factory.Autowired;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public LectureController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(methods = {RequestMethod.GET})
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("lecture-list", Map.of("lectures", lectureService.findAll()));
    }

    @RequestMapping(methods = {RequestMethod.POST})
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        JsonNode json = objectMapper.readTree(body);
        String name = json.path("name").asText("").trim();
        int price = json.path("price").asInt();
        boolean visible = json.path("visible").asBoolean(false);

        try {
            lectureService.create(name, price, visible);
            return new ModelAndView("redirect:/lectures");
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } catch (IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return null;
        }
    }

    @RequestMapping(methods = {RequestMethod.PUT})
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        JsonNode json = objectMapper.readTree(body);
        String originalName = request.getParameter("targetName");
        if (originalName == null || originalName.isBlank()) {
            originalName = request.getParameter("name");
        }

        String updatedName = json.path("name").asText("").trim();
        int updatedPrice = json.path("price").asInt();
        boolean updatedVisible = json.path("visible").asBoolean(false);

        try {
            Lecture updatedLecture = lectureService.update(originalName, updatedName, updatedPrice, updatedVisible);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(updatedLecture));
            return null;
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } catch (NoSuchElementException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } catch (IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return null;
        }
    }

    @RequestMapping(methods = {RequestMethod.DELETE})
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");

        try {
            lectureService.delete(name);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return null;
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } catch (NoSuchElementException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
