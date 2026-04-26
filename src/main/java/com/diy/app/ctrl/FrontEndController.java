package com.diy.app.ctrl;

import com.diy.app.Lecture;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@WebServlet("/")
public class FrontEndController extends HttpServlet {

    private final Map<String, Controller> controllerMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        controllerMap.put("GET:/lectures", new GetController());
        controllerMap.put("POST:/lectures", new PostController());
        controllerMap.put("PUT:/lectures", new PostController());
        controllerMap.put("DELETE:/lectures", new PostController());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String method = req.getMethod();
        String uri = req.getRequestURI();
        Controller controller = controllerMap.get(method + ":" + uri);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        try {
            controller.handleRequest(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
