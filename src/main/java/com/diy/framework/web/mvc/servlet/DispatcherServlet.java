package com.diy.framework.web.mvc.servlet;

import com.diy.framework.web.config.WebConfig;
import com.diy.framework.web.mvc.view.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet {
    private final Map<String, Controller> controllerMap;
    private final List<ViewResolver> viewResolvers;

    public DispatcherServlet() {
        controllerMap = new HashMap<>();
        viewResolvers = new ArrayList<>();
        viewResolvers.add(WebConfig.urlBasedViewResolver());
        viewResolvers.add(WebConfig.jspViewResolver());
    }

    public DispatcherServlet(Map<String, Controller> controllerMap) {
        this();
        this.controllerMap.putAll(controllerMap);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Map<String, ?> params = parseParams(req);
        System.out.println("DispatcherServlet service");

        String uri = req.getRequestURI();
        Controller controller = controllerMap.get(uri);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            ModelAndView mav = controller.handleRequest(req.getMethod(), params);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, ?> parseParams(final HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {});
        }
        return req.getParameterMap();
    }

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        final String viewName = mav.getViewName();

        View view = resolveView(viewName);
        if (view == null) {
            throw new ServletException("View not found: " + viewName);
        }
        view.render(mav.getModel(), req, res);
    }

    private View resolveView(final String viewName) {
        for (ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolveView(viewName);
            if (view != null) {
                return view;
            }
        }
        return null;
    }
}
