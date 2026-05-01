package com.diy.framework.web.servlet;

import com.diy.app.lecture.LectureController;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;
import com.diy.framework.web.mvc.view.ViewResolverFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> controllersMapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        controllersMapping.put("/lectures", new LectureController());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) {
        Controller controller = controllersMapping.get(req.getServletPath());
        if(controller == null) return;

        try {
            final ModelAndView mav = controller.handleRequest(req, resp);

            ViewResolver viewResolver = ViewResolverFactory.getViewResolver(mav.getViewName());

            render(viewResolver, mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final ViewResolver viewResolver,
                        final ModelAndView mav,
                        final HttpServletRequest req,
                        final HttpServletResponse resp) throws Exception {
        final String viewName = mav.getViewName();

        final View view = viewResolver.resolveViewName(viewName);

        if (view == null) {
            throw new RuntimeException("View not found: " + viewName);
        }

        view.render(mav.getModel(), req, resp);
    }
}

