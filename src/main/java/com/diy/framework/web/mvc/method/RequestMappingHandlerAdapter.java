package com.diy.framework.web.mvc.method;

import com.diy.framework.web.method.HandlerMethod;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.servlet.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return (handler instanceof HandlerMethod);
    }

    @Override
    public ModelAndView handle(final HttpServletRequest req, final HttpServletResponse res, final Object handler) throws Exception {
        return ((HandlerMethod) handler).handle(req, res);
    }
}
