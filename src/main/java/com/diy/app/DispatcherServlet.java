package com.diy.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        controllers.put("/lectures", new LectureController());
    }

//    @Override
//    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
//        final Map<String, ?> params = parseParams(req); // TODO: param에서 파싱한 결과를 가지고 Lecture 컨트롤러 호출
//    }
//
//    private Map<String, ?> parseParams(final HttpServletRequest req) throws IOException {
//        System.out.println(req.getRequestURL());
//        if ("application/json".equals(req.getHeader("Content-Type"))) {
//            final byte[] bodyBytes = req.getInputStream().readAllBytes();
//            final String body = new String(bodyBytes, StandardCharsets.UTF_8);
//
//            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {});
//        } else {
//            return req.getParameterMap();
//            return req.getRequestURL();
//        }
//    }
    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        StringBuffer url = req.getRequestURL();
        String path = url.substring(url.lastIndexOf("/"));
        Controller controller = controllers.get(path);
        try {
            controller.handleRequest(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}