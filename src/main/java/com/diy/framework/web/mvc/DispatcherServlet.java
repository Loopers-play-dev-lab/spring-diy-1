package com.diy.framework.web.mvc;

import com.diy.framework.web.context.ApplicationContext;
import com.diy.framework.web.mvc.handler.AnnotationHandlerAdapter;
import com.diy.framework.web.mvc.handler.AnnotationHandlerMapping;
import com.diy.framework.web.mvc.handler.HandlerAdapter;
import com.diy.framework.web.mvc.handler.HandlerMapping;
import com.diy.framework.web.mvc.handler.SimpleControllerHandlerAdapter;
import com.diy.framework.web.mvc.handler.SimpleControllerHandlerMapping;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final ViewResolver viewResolver = new DefaultViewResolver();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

        ApplicationContext applicationContext = new ApplicationContext("com.diy");

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(applicationContext);
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);

        SimpleControllerHandlerMapping simpleHandlerMapping = new SimpleControllerHandlerMapping();
        handlerMappings.add(simpleHandlerMapping);

        handlerAdapters.add(new AnnotationHandlerAdapter());
        handlerAdapters.add(new SimpleControllerHandlerAdapter());
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
        final String path = req.getRequestURI();
        final String key = req.getMethod() + " " + path;

        Object handler = null;
        for (HandlerMapping handlerMapping : handlerMappings) {
            handler = handlerMapping.getHandler(key);
            if (handler != null) break;
        }

        if (handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            final Map<String, ?> params = parseParams(req);

            for (HandlerAdapter handlerAdapter : handlerAdapters) {
                if (handlerAdapter.supports(handler)) {
                    final ModelAndView mav = handlerAdapter.handle(handler, params);
                    render(mav, req, resp);
                    return;
                }
            }

            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
