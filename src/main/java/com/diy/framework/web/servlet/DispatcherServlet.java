package com.diy.framework.web.servlet;

import com.diy.framework.controllers.factory.ControllerRegistry;
import com.diy.framework.web.mvc.IController;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private final ControllerRegistry controllerRegistry;
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    public DispatcherServlet(final ControllerRegistry controllerRegistry) {
        this.controllerRegistry = controllerRegistry;
        this.viewResolvers.add(new UrlBasedViewResolver());
        this.viewResolvers.add(new JspViewResolver());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) {
        final IController controller = controllerRegistry.get(req);
        if (controller == null) {
            throw new RuntimeException("404 Not Found");
        }

        try {
            final ModelAndView mav = controller.handleRequest(req, resp);
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
}
