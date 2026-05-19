package com.diy.framework.web.servlet;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;
import com.diy.framework.web.mvc.view.ViewResolverComposite;
import com.diy.framework.web.servlet.handler.adapter.HandlerAdapter;
import com.diy.framework.web.servlet.handler.adapter.HandlerAdapterComposite;
import com.diy.framework.web.servlet.handler.mapping.HandlerMapping;
import com.diy.framework.web.servlet.handler.mapping.HandlerMappingComposite;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private final HandlerMapping handlerMapping;
    private final HandlerAdapter handlerAdapter;
    private final ViewResolver viewResolver;

    public DispatcherServlet(ApplicationContext context) {
        this.handlerMapping = new HandlerMappingComposite(context);
        this.handlerAdapter = new HandlerAdapterComposite();
        this.viewResolver = new ViewResolverComposite();
    }

    @Override
    public void init() {
        handlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object handler = handlerMapping.getHandler(req);
            ModelAndView mav = handlerAdapter.handle(handler, req, resp);
            View view = viewResolver.resolveViewName(mav.getViewName());
            view.render(mav.getModel(), req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
