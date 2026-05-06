package com.diy.framework;

import com.diy.framework.view.JspViewResolver;
import com.diy.framework.view.UrlBasedViewResolver;
import com.diy.framework.view.View;
import com.diy.framework.view.ViewResolver;
import com.diy.framework.view.ViewResolverComposite;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> controllerMapping;

    private RequestParamParser requestParamParser;
    private ViewResolverComposite viewResolverComposite;

    public DispatcherServlet(Map<String, Controller> controllerMapping) {
        this.controllerMapping = controllerMapping;
    }

    @Override
    public void init() {
        this.requestParamParser = new RequestParamParser();

        List<ViewResolver> viewResolvers = List.of(new UrlBasedViewResolver(), new JspViewResolver());
        this.viewResolverComposite = new ViewResolverComposite(viewResolvers);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        Controller controller = controllerMapping.get(requestURI);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Map<String, ?> params = requestParamParser.parse(req);
            req.setAttribute("params", params);

            ModelAndView mav = controller.handleRequest(req, resp);

            View view = viewResolverComposite.resolve(mav.getViewName());
            view.render(mav.getModel(), req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
