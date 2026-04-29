package com.diy.app;

import com.diy.framework.web.Controller;
import com.diy.framework.web.model.Model;
import com.diy.framework.web.view.JspView;
import com.diy.framework.web.view.View;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureController implements Controller {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Map<Long, Lecture> repository = new HashMap<>();

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Map<String, ?> params = parseParams(request);

        // 강의 등록 로직
        if ("POST".equals(request.getMethod())) {
            final Lecture lecture = OBJECT_MAPPER.convertValue(params, Lecture.class);

            final long id = repository.size() + 1L;
            lecture.setId(id);
            repository.put(lecture.getId(), lecture);

            response.sendRedirect("/lectures");
        }
        // 강의 목록 로직
        else if ("GET".equals(request.getMethod())) {
            final Collection<Lecture> lectures = repository.values();

            final Model model = new Model();
            model.addAttribute("lectures", lectures);

            final View jspView = new JspView("lecture-list.jsp", model);
            jspView.render(request, response);
        }
    }

    private Map<String, ?> parseParams(final HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {});
        } else {
            return req.getParameterMap();
        }
    }
}
