package com.diy.framework;

import com.diy.framework.view.JspViewResolver;
import com.diy.framework.view.View;
import com.diy.framework.view.ViewResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private RequestMapping requestMapping;
    private ViewResolver viewResolver;

    @Override
    public void init() {
        requestMapping = new RequestMapping();
        requestMapping.initMapping();

        ServletContext servletContext = getServletContext();
        this.viewResolver = new JspViewResolver(servletContext);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String requestURI = req.getRequestURI();
        HandlerKey key = new HandlerKey(method, requestURI);

        Controller controller = requestMapping.getController(key);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Map<String, ?> params = parseParams(req);
            ModelAndView mav = controller.handleRequest(params);
            View view = viewResolver.resolveViewName(mav.getViewName());
            view.render(req, resp, mav.getModel());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
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
}
