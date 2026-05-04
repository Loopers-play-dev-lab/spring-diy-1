package com.diy.app.lecture.controller;

import com.diy.app.lecture.service.LectureService;
import com.diy.framework.web.beans.factory.Autowired;
import com.diy.framework.web.beans.factory.Component;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LectureCreateController implements Controller {

    private final LectureService lectureService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public LectureCreateController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        JsonNode json = mapper.readTree(body);
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
}
