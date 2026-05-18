package com.diy.framework.servlet;

import com.diy.framework.controller.Controller;
import com.diy.framework.controller.RequestHandler;
import com.diy.framework.controller.RequestHandlerImpl;
import com.diy.framework.controller.RequestKey;
import com.diy.framework.enums.RequestMethod;
import com.diy.framework.value.ModelAndView;
import com.diy.framework.view.View;
import com.diy.framework.view.resolver.ViewResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Request;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DispatcherServlet extends HttpServlet {

//    private final Map<String, Controller> controllerMapping;
    private final Map<RequestKey, RequestHandler> handlers;
    private final List<ViewResolver> viewResolvers;

    public DispatcherServlet(/*Map<String, Controller> controllerMapping,*/ Map<RequestKey, RequestHandler> handlers, List<ViewResolver> viewResolvers) {
        this.handlers = handlers;
//        this.controllerMapping = controllerMapping;
        this.viewResolvers = viewResolvers;
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) {
        final RequestKey requestKey = new RequestKey(req.getRequestURI(), RequestMethod.valueOf(req.getMethod()));
        final RequestHandler handler = handlers.get(requestKey);
//        final Controller controller = controllerMapping.get(req.getRequestURI());

        if (handler == null) {
            return;
        }

        try {
            final ModelAndView modelAndView = handler.handleRequest(req, resp);
            render(modelAndView, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, ?> parseParams(final HttpServletRequest request) throws IOException {
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            final byte[] bodyBytes = request.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<>() {});
        } else {
            return request.getParameterMap();
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final String viewName = modelAndView.viewName();
        final View view = viewResolvers.stream()
                .map(resolver -> resolver.resolveViewName(viewName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View not found: " + viewName));
        view.render(modelAndView.model(), req, resp);
    }
}
