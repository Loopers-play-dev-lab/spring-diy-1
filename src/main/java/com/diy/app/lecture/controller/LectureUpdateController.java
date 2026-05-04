package com.diy.app.lecture.controller;

import com.diy.app.lecture.Lecture;
import com.diy.app.lecture.LectureService;
import com.diy.framework.Autowired;
import com.diy.framework.Component;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LectureUpdateController implements Controller {

    private final LectureService lectureService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public LectureUpdateController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        JsonNode json = mapper.readTree(body);
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
            response.getWriter().write(mapper.writeValueAsString(updatedLecture));
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
}
