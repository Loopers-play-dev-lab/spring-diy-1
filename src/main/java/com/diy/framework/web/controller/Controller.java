package com.diy.framework.web.controller;

import com.diy.framework.web.HttpRequestMethod;
import com.diy.framework.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {

    ModelAndView handleRequest(final HttpRequestMethod httpRequestMethod, final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}
