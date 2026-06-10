package com.diy.framework.web.servlet;

import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver {

    ModelAndView resolveException(final HttpServletRequest request,
                                  final HttpServletResponse response,
                                  final Object handler,
                                  final Exception ex);
}
