package com.diy.framework.web.servlet;

import com.diy.framework.beans.factory.BeanFactoryUtils;
import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.support.WebApplicationContextUtils;
import com.diy.framework.core.Ordered;
import com.diy.framework.web.http.resolver.HandlerExceptionResolver;
import com.diy.framework.web.interceptor.HandlerInterceptor;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;
    private List<ViewResolver> viewResolvers;
    private List<HandlerInterceptor> handlerInterceptors;
    private List<HandlerExceptionResolver> handlerExceptionResolvers;

    @Override
    public void init() throws ServletException {
        initStrategies(initWebApplicationContext());
        super.init();
    }

    private ApplicationContext initWebApplicationContext() {
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext(), ApplicationContext.APPLICATION_CONTEXT_ATTRIBUTE);
    }

    private void initStrategies(final ApplicationContext context) {
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initViewResolvers(context);
        initHandlerInterceptors(context);
        initHandlerExceptionResolvers(context);
    }

    private void initHandlerMappings(final ApplicationContext context) {
        final Map<String, HandlerMapping> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class);
        this.handlerMappings = new ArrayList<>(matchingBeans.values());

        this.handlerMappings.sort(Comparator.comparingInt(o -> ((Ordered) o).getOrder()));
    }

    private void initHandlerAdapters(final ApplicationContext context) {
        final Map<String, HandlerAdapter> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class);
        this.handlerAdapters = new ArrayList<>(matchingBeans.values());
    }

    private void initViewResolvers(final ApplicationContext context) {
        final Map<String, ViewResolver> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ViewResolver.class);
        this.viewResolvers = new ArrayList<>(matchingBeans.values());

        this.viewResolvers.sort(Comparator.comparingInt(o -> ((Ordered) o).getOrder()));
    }

    private void initHandlerInterceptors(final ApplicationContext context) {
        final Map<String, HandlerInterceptor> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerInterceptor.class);
        this.handlerInterceptors = new ArrayList<>(matchingBeans.values());
    }

    private void initHandlerExceptionResolvers(final ApplicationContext context) {
        final Map<String, HandlerExceptionResolver> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class);
        this.handlerExceptionResolvers = new ArrayList<>(matchingBeans.values());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        Object handler = null;
        try {
            preHandle(req, resp);

            handler = getHandler(req);

            final HandlerAdapter ha = getHandlerAdapter(handler);

            final ModelAndView mv = ha.handle(req, resp, handler);

            if (mv == null) return;

            postHandle(req, resp, mv);

            render(mv, req, resp);
        } catch (Exception e) {
            Exception exception = e;
            if (exception instanceof InvocationTargetException) {
                exception = (Exception) exception.getCause();
            }
            boolean caught = false;
            for (HandlerExceptionResolver handlerExceptionResolver : handlerExceptionResolvers) {
                ModelAndView modelAndView = handlerExceptionResolver.resolveException(req, resp, handler, exception);
                if (modelAndView != null) {
                    caught = true;
                    break;
                }
            }
            if (!caught) throw new RuntimeException(exception);
        } finally {
            afterCompletion(req, resp);
        }
    }

    protected Object getHandler(final HttpServletRequest req) throws Exception {
        if (this.handlerMappings != null) {
            for (final HandlerMapping mapping : this.handlerMappings) {
                final Object handler = mapping.getHandler(req);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    protected HandlerAdapter getHandlerAdapter(final Object handler) throws ServletException {
        if (this.handlerAdapters != null) {
            for (final HandlerAdapter adapter : this.handlerAdapters) {
                if (adapter.supports(handler)) {
                    return adapter;
                }
            }
        }

        throw new ServletException("No adapter for handler [" + handler +
                "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
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
        if (this.viewResolvers != null) {
            for (final ViewResolver viewResolver : this.viewResolvers) {
                final View view = viewResolver.resolveViewName(viewName);
                if (view != null) {
                    return view;
                }
            }
        }

        return null;
    }

    private void preHandle(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        if (handlerInterceptors.stream().anyMatch(interceptor -> !interceptor.preHandle(req, resp))) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void postHandle(final HttpServletRequest req, final HttpServletResponse resp, ModelAndView mv) {
        handlerInterceptors.forEach(interceptor -> interceptor.postHandle(req, resp, mv));
    }

    private void afterCompletion(final HttpServletRequest req, final HttpServletResponse resp) {
        handlerInterceptors.forEach(interceptor -> interceptor.afterCompletion(req, resp));
    }
}
