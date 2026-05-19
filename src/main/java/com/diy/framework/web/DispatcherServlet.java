package com.diy.framework.web;

import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.view.JspViewResolver;
import com.diy.framework.web.view.ModelAndView;
import com.diy.framework.web.view.View;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class DispatcherServlet extends HttpServlet {

    private HandlerMapping handlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;
    private final JspViewResolver viewResolver = new JspViewResolver();
    private final List<HandlerAdapter> handlerAdapters = List.of(
            new ControllerHandlerAdapter(),
            new HandlerExecutionAdapter()
    );

    protected abstract String[] getBasePackages();

    protected abstract void initHandlerMappings(HandlerMapping handlerMapping)
            throws InvocationTargetException, IllegalAccessException;

    @Override
    public void init() {
        handlerMapping = new HandlerMapping();
        try {
            initHandlerMappings(handlerMapping);
            BeanFactory beanFactory = new BeanFactory(getBasePackages());
            annotationHandlerMapping = new AnnotationHandlerMapping(beanFactory);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
        try {
            Object handler = handlerMapping.getController(req);
            if (handler == null) {
                handler = annotationHandlerMapping.getHandler(req);
            }
            if (handler == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            HandlerAdapter adapter = handlerAdapters.stream()
                    .filter(a -> a.supports(handler))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No adapter found for: " + handler.getClass()));

            final ModelAndView mav = adapter.handle(req, resp, handler);
            if (mav != null) {
                render(mav, req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse resp)
            throws Exception {
        final View view = viewResolver.resolveViewName(mav.getViewName());
        if (view == null) {
            throw new RuntimeException("View not found: " + mav.getViewName());
        }
        view.render(mav.getModel(), req, resp);
    }
}
