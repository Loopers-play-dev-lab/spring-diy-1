package com.diy.framework.web.servlet;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.HtmlViewResolver;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;
import com.diy.framework.web.servlet.handler.AnnotationHandlerMapping;
import com.diy.framework.web.servlet.handler.ControllerHandlerMapping;
import com.diy.framework.web.servlet.handler.adapter.AnnotationHandlerAdapter;
import com.diy.framework.web.servlet.handler.adapter.ControllerHandlerAdapter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet implements ServletContextInitializer {

    private final ApplicationContext context;
    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;
    private List<ViewResolver> viewResolvers;

    public DispatcherServlet(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addServlet("dispatcherServlet", this).addMapping("/");
    }

    @Override
    public void init() {
        handlerMappings = List.of(
                new AnnotationHandlerMapping(context),
                new ControllerHandlerMapping(context)
        );
        handlerAdapters = List.of(
                new AnnotationHandlerAdapter(),
                new ControllerHandlerAdapter()
        );
        viewResolvers = List.of(
                new UrlBasedViewResolver(),
                new JspViewResolver(),
                new HtmlViewResolver()
        );
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object handler = handlerMappings.stream()
                    .map(mapping -> mapping.getHandler(req))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No handler found"));

            HandlerAdapter adapter = handlerAdapters.stream()
                    .filter(a -> a.supports(handler))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No adapter found"));

            ModelAndView mav = adapter.handle(req, resp, handler);

            View view = viewResolvers.stream()
                    .map(resolver -> resolver.resolveViewName(mav.getViewName()))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("View not found: " + mav.getViewName()));

            view.render(mav.getModel(), req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
