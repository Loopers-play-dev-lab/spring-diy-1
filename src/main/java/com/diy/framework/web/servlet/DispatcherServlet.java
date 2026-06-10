package com.diy.framework.web.servlet;

import com.diy.framework.beans.factory.BeanFactoryUtils;
import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.support.WebApplicationContextUtils;
import com.diy.framework.core.Ordered;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;
    private List<ViewResolver> viewResolvers;
    private List<HandlerInterceptor> interceptors;
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
        initInterceptors(context);
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

    private void initInterceptors(final ApplicationContext context) {
        this.interceptors = getOptionalBeans(context, HandlerInterceptor.class);
    }

    private void initHandlerExceptionResolvers(final ApplicationContext context) {
        this.handlerExceptionResolvers = getOptionalBeans(context, HandlerExceptionResolver.class);
    }

    private <T> List<T> getOptionalBeans(final ApplicationContext context, final Class<T> type) {
        try {
            return new ArrayList<>(BeanFactoryUtils.beansOfTypeIncludingAncestors(context, type).values());
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(final HttpServletRequest req, final HttpServletResponse resp) {
        Object handler = null;
        int interceptorIndex = -1;

        try {
            ModelAndView mv;

            try {
                handler = getHandler(req);

                final HandlerAdapter ha = getHandlerAdapter(handler);

                for (int i = 0; i < this.interceptors.size(); i++) {
                    if (!this.interceptors.get(i).preHandle(req, resp, handler)) {
                        triggerAfterCompletion(interceptorIndex, req, resp, handler, null);
                        return;
                    }
                    interceptorIndex = i;
                }

                mv = ha.handle(req, resp, handler);

                for (int i = this.interceptors.size() - 1; i >= 0; i--) {
                    this.interceptors.get(i).postHandle(req, resp, handler, mv);
                }
            } catch (Exception ex) {
                mv = processHandlerException(req, resp, handler, ex);
                if (mv == null) {
                    throw ex;
                }
            }

            if (mv != null && mv.getViewName() != null) {
                render(mv, req, resp);
            }

            triggerAfterCompletion(interceptorIndex, req, resp, handler, null);
        } catch (Exception ex) {
            triggerAfterCompletion(interceptorIndex, req, resp, handler, ex);
            throw new RuntimeException(ex);
        }
    }

    private ModelAndView processHandlerException(final HttpServletRequest req,
                                                 final HttpServletResponse resp,
                                                 final Object handler,
                                                 final Exception ex) {
        if (this.handlerExceptionResolvers != null) {
            for (final HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
                final ModelAndView mv = resolver.resolveException(req, resp, handler, ex);
                if (mv != null) {
                    return mv;
                }
            }
        }
        return null;
    }

    private void triggerAfterCompletion(final int interceptorIndex,
                                        final HttpServletRequest req,
                                        final HttpServletResponse resp,
                                        final Object handler,
                                        final Exception ex) {
        for (int i = interceptorIndex; i >= 0; i--) {
            try {
                this.interceptors.get(i).afterCompletion(req, resp, handler, ex);
            } catch (Exception e) {
                System.err.println("HandlerInterceptor.afterCompletion threw exception: " + e);
            }
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
}
