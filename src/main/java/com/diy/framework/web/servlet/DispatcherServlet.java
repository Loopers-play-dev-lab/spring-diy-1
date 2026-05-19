package com.diy.framework.web.servlet;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.View;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private final HandlerMappingComposite handlerMappingComposite;
    private final HandlerAdapterComposite handlerAdapterComposite;
    private final ViewResolverComposite viewResolverComposite;

    public DispatcherServlet(ApplicationContext context) {
        this.handlerMappingComposite = new HandlerMappingComposite(context);
        this.handlerAdapterComposite = new HandlerAdapterComposite();
        this.viewResolverComposite = new ViewResolverComposite();
    }

    @Override
    public void init() {
        handlerMappingComposite.initialize();
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object handler = handlerMappingComposite.getHandler(req);
            ModelAndView mav = handlerAdapterComposite.handle(handler, req, resp);
            View view = viewResolverComposite.resolve(mav.getViewName());
            view.render(mav.getModel(), req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
