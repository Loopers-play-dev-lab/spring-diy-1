package com.diy.framework.web.interceptor;

import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    boolean preHandle(final HttpServletRequest req, final HttpServletResponse resp);
    void postHandle(final HttpServletRequest req, final HttpServletResponse resp, ModelAndView mv);
    void afterCompletion(final HttpServletRequest req, final HttpServletResponse resp);
}
