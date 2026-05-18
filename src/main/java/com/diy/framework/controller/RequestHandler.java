package com.diy.framework.controller;

import com.diy.framework.value.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface RequestHandler {
    ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}
