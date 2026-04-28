package com.diy.app;

import com.diy.app.controller.Controller;
import com.diy.app.controller.HandlerMapping;
import com.diy.app.controller.LectureController;
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
    final private HandlerMapping handlerMapping = new HandlerMapping();

    @Override
    public void init() throws ServletException {
        handlerMapping.register("/lectures", new LectureController());
    }


    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String path = req.getRequestURI();
        final Controller controller = handlerMapping.getHandler(path);

        if (controller == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            controller.handleRequest(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
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
