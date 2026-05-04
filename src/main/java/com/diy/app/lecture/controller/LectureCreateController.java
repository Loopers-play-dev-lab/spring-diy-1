package com.diy.app.lecture.controller;

import com.diy.app.lecture.Lecture;
import com.diy.app.lecture.LectureRepository;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureCreateController implements Controller {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        JsonNode json = mapper.readTree(body);
        String name = json.path("name").asText("").trim();
        int price = json.path("price").asInt();
        boolean visible = json.path("visible").asBoolean(false);

        if (name.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        if (LectureRepository.containsName(name)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return null;
        }

        Lecture lecture = new Lecture(name, price, visible);
        LectureRepository.save(lecture);

        return new ModelAndView("redirect:/lectures");
    }
}
