package com.diy.framework.web.servlet;

import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface HandlerExecution {
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
