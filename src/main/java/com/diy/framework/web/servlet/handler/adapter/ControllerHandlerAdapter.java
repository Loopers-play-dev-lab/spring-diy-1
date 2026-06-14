package com.diy.framework.web.servlet.handler.adapter;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.servlet.HandlerAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ((Controller) handler).handleRequest(request, response);
    }
}
