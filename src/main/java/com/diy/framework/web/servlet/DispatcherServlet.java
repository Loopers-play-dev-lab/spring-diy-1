package com.diy.framework.web.servlet;

import com.diy.app.lecture.LectureController;
import com.diy.app.lecture.LectureRepository;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.view.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> controllersMapping = new HashMap<>();
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    public DispatcherServlet() {
        viewResolvers.add(new UrlBasedViewResolver());
        viewResolvers.add(new JspViewResolver());
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        controllersMapping.put("/lectures", new LectureController(new LectureRepository()));
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) {
        Controller controller = controllersMapping.get(req.getServletPath());
        if(controller == null) return;

        try {
            final ModelAndView mav = controller.handleRequest(req, resp);

            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final ModelAndView mav,
                        final HttpServletRequest req,
                        final HttpServletResponse resp) throws Exception {
        final String viewName = mav.getViewName();

        final View view = resolveViewName(viewName);

        if (view == null) {
            throw new RuntimeException("View not found: " + viewName);
        }

        view.render(mav.getModel(), req, resp);
    }

    private View resolveViewName(String viewName) {
        for(ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolveViewName(viewName);

            if(view != null) {
                return view;
            }
        }

        return null;
    }
}

