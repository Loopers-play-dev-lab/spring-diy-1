package com.diy.framework.web.mvc.servlet;

import com.diy.framework.web.mvc.view.ModelAndView;

import java.util.Map;

@FunctionalInterface
public interface Controller {
    ModelAndView handleRequest(final String method, final Map<String, ?> params) throws IllegalArgumentException;
}
