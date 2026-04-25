package com.diy.app.servlet;

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
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Map<String, ?> params = parseParams(req);

        System.out.println("req.getMethod() = " + req.getMethod());
        System.out.println("req.getRequestURI() = " + req.getRequestURI());
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
