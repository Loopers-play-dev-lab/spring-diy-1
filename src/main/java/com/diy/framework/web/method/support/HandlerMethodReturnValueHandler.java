package com.diy.framework.web.method.support;

import com.diy.framework.web.method.HandlerMethod;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodReturnValueHandler {

    boolean supportsReturnType(final HandlerMethod handlerMethod);

    ModelAndView handleReturnValue(final Object returnValue,
                                   final HandlerMethod handlerMethod,
                                   final HttpServletRequest request,
                                   final HttpServletResponse response) throws Exception;
}
