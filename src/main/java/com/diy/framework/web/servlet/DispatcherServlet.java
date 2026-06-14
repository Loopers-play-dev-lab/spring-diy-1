package com.diy.framework.web.servlet;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.core.Ordered;
import com.diy.framework.web.context.WebApplicationContextUtils;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet implements ServletContextInitializer {

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;
    private List<ViewResolver> viewResolvers;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addServlet("dispatcherServlet", this).addMapping("/");
    }

    @Override
    public void init() {
        ApplicationContext context = WebApplicationContextUtils.getApplicationContext(getServletContext());

        handlerMappings = context.getBeansOfType(HandlerMapping.class).values().stream()
                .sorted(Comparator.comparingInt(mapping -> {
                    if (mapping instanceof Ordered o) {
                        return o.getOrder();
                    }
                    return Ordered.LOWEST_PRECEDENCE;
                }))
                .toList();

        handlerAdapters = new ArrayList<>(context.getBeansOfType(HandlerAdapter.class).values());

        viewResolvers = new ArrayList<>(context.getBeansOfType(ViewResolver.class).values());
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
                    .orElseThrow(() -> new RuntimeException("View not found"));

            view.render(mav.getModel(), req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
