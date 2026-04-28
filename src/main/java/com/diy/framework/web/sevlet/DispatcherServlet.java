package com.diy.framework.web.sevlet;

import com.diy.app.Lecture;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Map<Long, Lecture> repository = new HashMap<>();

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final Map<String, ?> params = parseParams(req);

        if(req.getRequestURI().startsWith("/lectures")) {
            // 강의 등록 로직
            if ("POST".equals(req.getMethod())) {
                final Lecture lecture = OBJECT_MAPPER.convertValue(params, Lecture.class);

                final long id = repository.size() + 1L;
                lecture.setId(id);
                repository.put(lecture.getId(), lecture);

                resp.sendRedirect("/lectures");
            }
            // 강의 목록 로직
            else if ("GET".equals(req.getMethod())) {
                final Collection<Lecture> lectures = repository.values();

                req.setAttribute("lectures", lectures);
                req.getRequestDispatcher("lecture-list.jsp").forward(req, resp);
            }
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
