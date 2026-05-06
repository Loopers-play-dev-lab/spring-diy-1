package com.diy.framework.web.mvc;

import com.diy.app.LectureController;
import com.diy.app.LectureRepository;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.DefaultViewResolver;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final Map<String, Controller> controllers = new HashMap<>();
    private final ViewResolver viewResolver = new DefaultViewResolver();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        final LectureRepository lectureRepository = new LectureRepository();
        final LectureController lectureController = new LectureController(lectureRepository);
        controllers.put("GET /lectures", lectureController::list);
        controllers.put("POST /lectures", lectureController::create);
        controllers.put("PUT /lectures", lectureController::update);
        controllers.put("DELETE /lectures", lectureController::delete);
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

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String url = req.getRequestURL().toString();
        final String path = url.substring(url.lastIndexOf("/"));
        final String key = req.getMethod() + " " + path;
        final Controller controller = controllers.get(key);

        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            final Map<String, ?> params = parseParams(req);
            final ModelAndView mav = controller.handleRequest(params);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final String viewName = mav.getViewName();

        final View view = viewResolver.resolve(viewName);

        if (view == null) {
            throw new RuntimeException("View not found: " + viewName);
        }

        view.render(mav.getModel(), req, resp);
    }
}
