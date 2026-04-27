package com.diy.app.lecture.controller;

import com.diy.app.lecture.Lecture;
import com.diy.app.lecture.LectureStorage;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class LectureUpdateController implements Controller {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        Lecture updated = mapper.readValue(body, Lecture.class);

        Lecture target = LectureStorage.LECTURES.stream()
                .filter(l -> l.getId().equals(updated.getId()))
                .findFirst()
                .orElse(null);

        if (target == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        target.setName(updated.getName());
        target.setPrice(updated.getPrice());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(target));
        return null;
    }
}
