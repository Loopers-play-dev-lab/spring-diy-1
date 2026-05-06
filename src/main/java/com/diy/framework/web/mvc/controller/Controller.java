package com.diy.framework.web.mvc.controller;

import com.diy.framework.web.mvc.ModelAndView;

import java.util.Map;

@FunctionalInterface
public interface Controller {
    ModelAndView handleRequest(final Map<String, ?> params) throws Exception;
}
