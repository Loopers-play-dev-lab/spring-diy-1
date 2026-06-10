package com.diy.framework.web.mvc.servlet;

import com.diy.framework.web.beans.annotations.Controller;
import com.diy.framework.web.beans.annotations.RestController;
import com.diy.framework.web.beans.factory.ApplicationContext;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet {
    private final Map<String, Object> controllerMap = new HashMap<>();
    private final Map<String, Object> restControllerMap = new HashMap<>();
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ApplicationContext context = ApplicationContext.getInstance();
        try {
            context.setConfigurations();
            context.setBeans();
            controllerMap.putAll(context.getControllerMap(Controller.class));
            restControllerMap.putAll(context.getControllerMap(RestController.class));
            viewResolvers.addAll(context.getBeans("ViewResolver").stream().map(ViewResolver.class::cast).toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Map<String, ?> params = parseParams(req);
        System.out.println("DispatcherServlet service");

        String uri = req.getRequestURI();
        Object controller = controllerMap.get(uri);
        Object restController = restControllerMap.get(uri);

        if (controller == null && restController != null) {
            Method method = null;
            Object result = null;
            try {
                method = restController.getClass().getMethod("handleRequest", String.class, Map.class);
                result = method.invoke(restController, req.getMethod(), params);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter writer = resp.getWriter();
            writer.print(new ObjectMapper().writeValueAsString(result));
            writer.flush();
            return;
        }

        if (controller == null && restController == null) {
            StringBuilder viewName = new StringBuilder(uri.substring(1));
            if (!params.isEmpty()) {
                viewName.append("?");
                params.forEach((k, v) -> viewName.append(k).append("=").append(v).append("&"));
                viewName.deleteCharAt(viewName.length() - 1);
            }
            ModelAndView mav = new ModelAndView(uri.substring(1));
            render(mav, req, resp);
            return;
        }
        try {
            Method method = controller.getClass().getMethod("handleRequest", String.class, Map.class);
            ModelAndView mav = (ModelAndView) method.invoke(controller, req.getMethod(), params);
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
        if (view == null && !viewName.equals("none")) {
            throw new ServletException("View not found: " + viewName);
        }
        if (viewName.equals("none")) {
            if (req.getMethod().equals("PUT")) {
                res.setStatus(HttpServletResponse.SC_SEE_OTHER);
                res.setHeader("Location", req.getRequestURI());
            }
            return;
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
