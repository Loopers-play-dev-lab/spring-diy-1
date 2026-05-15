package com.diy.framework.web.mvc;

import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.RedirectViewResolver;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final HandlerMapping handlerMapping = HandlerMapping.getInstance();
    private final List<ViewResolver> viewResolvers = Arrays.asList(
            new RedirectViewResolver(),
            new JspViewResolver()
    );

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        final Controller controller = handlerMapping.getHandler(request);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            final ModelAndView mav = controller.handleRequest(request, response);
            if (mav == null) {
                return;
            }
            render(mav, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String viewName = mav.getViewName();
        for (ViewResolver resolver : viewResolvers) {
            final View view = resolver.resolveViewName(viewName);
            if (view != null) {
                view.render(mav.getModel(), request, response);
                return;
            }
        }
        throw new RuntimeException("View not found: " + viewName);
    }
}
