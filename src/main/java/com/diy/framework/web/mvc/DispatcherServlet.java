package com.diy.framework.web.mvc;

import com.diy.app.LectureController;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> controllers = new HashMap<>();
    private final ViewResolver viewResolver = new JspViewResolver();

    @Override
    public void init() {
        controllers.put("/lectures", new LectureController());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String uri = req.getRequestURI();
        final Controller controller = controllers.get(uri);

        try {
            final ModelAndView mav = controller.handleRequest(req, resp);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final String viewName = mav.getViewName();
        final View view = viewResolver.resolveViewName(viewName);

        if (view == null) {
            throw new RuntimeException("View not found: " + viewName);
        }

        view.render(mav.getModel(), req, resp);
    }
}
