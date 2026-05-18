package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;

import java.util.Map;

public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object handler, Map<String, ?> params) throws Exception {
        return ((Controller) handler).handleRequest(params);
    }
}
