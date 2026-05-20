package com.diy.framework.web.servlet;

import com.diy.framework.web.mvc.HandlerAdapter;
import com.diy.framework.web.mvc.HandlerMapping;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private final List<ViewResolver> viewResolvers;

    public DispatcherServlet(final List<HandlerMapping> handlerMappings,
                             final List<HandlerAdapter> handlerAdapters,
                             final List<ViewResolver> viewResolvers) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
        this.viewResolvers = viewResolvers;
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final Object handler = getHandler(req);

        if (handler == null) {
            return;
        }

        try {
            final HandlerAdapter adapter = getAdapter(handler);
            final ModelAndView mav = adapter.handle(req, resp, handler);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getHandler(final HttpServletRequest req) {
        return handlerMappings.stream()
                .map(hm -> hm.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private HandlerAdapter getAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(ha -> ha.supports(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No HandlerAdapter found for handler: " + handler.getClass()));
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
        for (final ViewResolver viewResolver : viewResolvers) {
            final View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }
        return null;
    }
}
