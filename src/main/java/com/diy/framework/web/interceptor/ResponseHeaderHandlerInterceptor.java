package com.diy.framework.web.interceptor;

import com.diy.framework.context.annotation.Component;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ResponseHeaderHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(final HttpServletRequest req, final HttpServletResponse resp) {
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest req, final HttpServletResponse resp, ModelAndView mv) {
        resp.setHeader("X-App-Version", "1.0.0");
        resp.setHeader("X-Server-Name", "Spring-DIY");
    }

    @Override
    public void afterCompletion(final HttpServletRequest req, final HttpServletResponse resp) {

    }
}
