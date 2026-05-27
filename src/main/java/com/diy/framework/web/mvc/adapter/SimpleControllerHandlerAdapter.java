package com.diy.framework.web.mvc.adapter;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.HandlerAdapter;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest req, final HttpServletResponse resp, final Object handler) throws Exception {
        return ((Controller) handler).handleRequest(req, resp);
    }
}
