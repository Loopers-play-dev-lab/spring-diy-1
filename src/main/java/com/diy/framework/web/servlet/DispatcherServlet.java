package com.diy.framework.web.servlet;

import com.diy.framework.web.annotations.RequestMethod;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    private final Map<RequestHandlerKey, HandlerExecution> handlerExecutions;
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    public DispatcherServlet(final Map<RequestHandlerKey, HandlerExecution> handlerExecutions) {
        this.handlerExecutions = handlerExecutions;
        this.viewResolvers.add(new UrlBasedViewResolver());
        this.viewResolvers.add(new JspViewResolver());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String uri = normalizePath(req.getRequestURI());
        final RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());

        final HandlerExecution handlerExecution = handlerExecutions.get(new RequestHandlerKey(uri, requestMethod));

        if (handlerExecution == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            final ModelAndView mav = handlerExecution.handle(req, resp);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final String viewName = mav.getViewName();

        final View view = resolveViewName(viewName);

        if (view == null) {
            throw new RuntimeException("View not found: " + viewName);
        }

        view.render(mav.getModel(), req, resp);
    }

    private View resolveViewName(final String viewName) {
        for (final ViewResolver viewResolver : this.viewResolvers) {
            final View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }

        return null;
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

    private String normalizePath(final String path) {
        if (path == null || path.isBlank() || "/".equals(path)) {
            return "/";
        }

        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        if (normalizedPath.endsWith("/")) {
            return normalizedPath.substring(0, normalizedPath.length() - 1);
        }

        return normalizedPath;
    }
}
