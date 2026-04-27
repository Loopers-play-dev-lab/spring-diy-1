package com.diy.app.controller;

import com.diy.framework.web.ModelAndView;
import com.diy.framework.web.controller.Controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class LectureController implements Controller {
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

        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Map<String, Object> model = parseParams(req);

        return new ModelAndView("lecture-list", model);
    }

    private Map<String, Object> parseParams(final HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<>() {});
        }
//        else {
//            return req.getParameterMap();
//        }
        throw new RuntimeException("400 Bad Request");
    }
}
