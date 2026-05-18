package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.ModelAndView;

import java.util.Map;

public interface HandlerAdapter {
    boolean supports(Object handler);
    ModelAndView handle(Object handler, Map<String, ?> params) throws Exception;
}
