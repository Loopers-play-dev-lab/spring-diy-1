package com.diy.framework.web.server;

import com.diy.framework.web.Controller;
import com.diy.framework.web.HandlerMapping;
import com.diy.framework.web.mvc.Model;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;
import com.diy.framework.web.mvc.view.ViewResolverRegistry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private HandlerMapping handlerMapping;
    private ViewResolverRegistry viewResolvers;

    @Override
    public void init() throws ServletException {
        handlerMapping = (HandlerMapping) getServletContext().getAttribute("handlerMapping");
        viewResolvers = (ViewResolverRegistry) getServletContext().getAttribute("viewResolvers");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Controller controller = handlerMapping.getHandler(req.getRequestURI());
        if(controller == null) {
            resp.sendError(404);
            return;
        }
        try {
            Model model = new Model();
            String viewName = controller.handleRequest(req, resp, model);
            if (viewName != null) {
                View view = resolveView(viewName);
                if (view != null) {
                    view.render(req, resp, model);
                }
                else {
                    resp.sendError(404);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private View resolveView(String viewName) {
        for (ViewResolver resolver : viewResolvers.getResolvers()) {
            View view = resolver.resolve(viewName, getServletContext());
            if (view != null) {
                return view;
            }
        }
        return null;
    }
}
