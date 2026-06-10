package com.diy.framework.web.servlet;

import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    default boolean preHandle(final HttpServletRequest request,
                              final HttpServletResponse response,
                              final Object handler) throws Exception {
        return true;
    }

    default void postHandle(final HttpServletRequest request,
                            final HttpServletResponse response,
                            final Object handler,
                            final ModelAndView modelAndView) throws Exception {
    }

    default void afterCompletion(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final Object handler,
                                 final Exception ex) throws Exception {
    }
}
