package com.diy.framework;

import com.diy.framework.view.*;
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
import java.util.List;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private RequestMapping requestMapping;
    private List<ViewResolver> viewResolvers;

    @Override
    public void init() {
        requestMapping = new RequestMapping();
        requestMapping.initMapping();

        ServletContext servletContext = getServletContext();
        viewResolvers = List.of(
                new RedirectViewResolver(),
                new JspViewResolver(servletContext)
        );
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        Controller controller = requestMapping.getController(requestURI);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Map<String, ?> params = parseParams(req);
            ModelAndView mav = controller.handleRequest(req, resp, params);
            View view = resolveView(mav.getViewName());
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

    private View resolveView(String viewName) throws Exception {
        for (ViewResolver resolver : viewResolvers) {
            View view = resolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }

        throw new IllegalArgumentException(String.format("View Resolver를 찾을 수 없습니다. view name: %s", viewName));
    }
}
