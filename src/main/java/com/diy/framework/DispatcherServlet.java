package com.diy.framework;

import com.diy.app.lecture.LectureController;
import com.diy.app.lecture.LectureRepository;
import com.diy.app.lecture.LectureService;
import com.diy.framework.view.JspViewResolver;
import com.diy.framework.view.UrlBasedViewResolver;
import com.diy.framework.view.View;
import com.diy.framework.view.ViewResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> requestMapping;
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    public DispatcherServlet() {
        LectureRepository lectureRepository = new LectureRepository();
        LectureService lectureService = new LectureService(lectureRepository);
        LectureController lectureController = new LectureController(lectureService);

        this.requestMapping = Map.of("/lectures", lectureController);
        this.viewResolvers.add(new UrlBasedViewResolver());
        this.viewResolvers.add(new JspViewResolver());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        Controller controller = requestMapping.get(requestURI);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Map<String, ?> params = parseParams(req);
            req.setAttribute("params", params);

            ModelAndView mav = controller.handleRequest(req, resp);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, ?> parseParams(final HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {
            });
        } else {
            return req.getParameterMap();
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest req,
            final HttpServletResponse resp) throws Exception {
        final String viewName = mav.getViewName();
        final View view = resolveViewName(viewName);
        view.render(mav.getModel(), req, resp);
    }

    private View resolveViewName(final String viewName) {
        for (final ViewResolver viewResolver : this.viewResolvers) {
            final View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }

        throw new RuntimeException("View not found: " + viewName);
    }
}
