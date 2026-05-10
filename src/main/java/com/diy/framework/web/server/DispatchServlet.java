package com.diy.framework.web.server;

import com.diy.app.LectureController;
import com.diy.framework.web.server.mv.JspViewResolver;
import com.diy.framework.web.server.mv.ModelAndView;
import com.diy.framework.web.server.mv.ViewResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class DispatchServlet extends HttpServlet {

    private static Map<String, Controller> controllers;
    private static final ViewResolver viewResolver = new JspViewResolver();

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
            final var mav = controller.handleRequest(req, resp);
            render(req, resp, mav);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView mav) throws Exception {
        final var viewName = mav.getViewName();
        final var view = viewResolver.resolve(viewName);
        if (view == null) {
            throw new RuntimeException("[DispatcherServlet] view not found: " + viewName);
        }
        view.render(req, resp, mav);
    }
}
