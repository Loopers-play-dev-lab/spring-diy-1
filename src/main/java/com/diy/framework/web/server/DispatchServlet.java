package com.diy.framework.web.server;

import com.diy.app.LectureController;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class DispatchServlet extends HttpServlet {

    private static Map<String, Controller> controllers;

    @Override
    public void init() {
        System.out.println("[DispatcherServlet] init() is called.");
        controllers = new HashMap<>();
        controllers.put("/lectures", new LectureController());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) {
        System.out.println("[DispatcherServlet] service() is called.");

        final var uri = req.getRequestURI();
        final var controller = controllers.getOrDefault(uri, null);

        if (controller == null) {
            System.out.println("[DispatcherServlet] no such controller: " + uri);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            System.out.println("[DispatcherServlet] request URI: " + uri + ", controller: " + controller);
            controller.handleRequest(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
