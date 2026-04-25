package com.diy.app.servlet;

import com.diy.app.domain.Lecture;
import com.diy.app.service.LectureService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private static final LectureService service = LectureService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Map<String, ?> params = parseParams(req);

        String method = req.getMethod();
        System.out.println("req.getMethod() = " + method);
        String uri = req.getRequestURI();
        System.out.println("req.getRequestURI() = " + uri);

        if (!uri.startsWith("/lectures")) return;

        if (method.equals(HttpMethod.GET.getValue())) {
            String[] uriArg = uri.split("/");
            if (uriArg.length == 3) {
                long id = Long.parseLong(uriArg[2]);
                req.setAttribute("lecture", service.getLectureById(id));
            } else if (uriArg.length == 2) {
                req.setAttribute("lectures", service.getAllLectures());
                req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
            }
        }
        if (method.equals(HttpMethod.POST.getValue())) {
            Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
            service.create(body.getName(), body.getPrice());

            resp.sendRedirect("/lectures");
        }
        if (method.equals(HttpMethod.PUT.getValue())) {
            Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
            service.update(body);
        }
        if (method.equals(HttpMethod.GET.getValue())) {
            String[] uriArg = uri.split("/");
            long id = Long.parseLong(uriArg[2]);
            req.setAttribute("lecture", service.getLectureById(id));
        }
    }

    private Map<String,?> parseParams(final HttpServletRequest req) throws IOException {
        if (ContentType.JSON.getValue().equals(req.getHeader(HttpHeader.CONTENT_TYPE.getValue()))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {
            });
        }

        return req.getParameterMap();
    }
}
