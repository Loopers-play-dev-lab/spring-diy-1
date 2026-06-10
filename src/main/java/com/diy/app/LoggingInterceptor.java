package com.diy.app;

import com.diy.framework.context.annotation.Component;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        System.out.println("[preHandle] " + request.getMethod() + " " + request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) {
        System.out.println("[postHandle] status=" + response.getStatus());
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                final Exception ex) {
        System.out.println("[afterCompletion] status=" + response.getStatus() + ", ex=" + ex);
    }
}
