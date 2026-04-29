package com.diy.framework.web.sevlet;

import com.diy.app.Lecture;
import com.diy.app.LectureController;
import com.diy.framework.web.Controller;
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

    private final Map<String, Controller> controllerMap = new HashMap<>();

    public DispatcherServlet() {
        controllerMap.put("/lectures", new LectureController());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) {
        String requestURI = req.getRequestURI();

        try {
            controllerMap.get(requestURI).handleRequest(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
