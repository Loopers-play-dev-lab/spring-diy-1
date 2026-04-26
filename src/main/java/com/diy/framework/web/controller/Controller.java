package com.diy.framework.web.controller;

import com.diy.framework.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {

    ModelAndView handleRequest(final HttpServletRequest req, final HttpServletResponse res) throws Exception;

    String getPath();
}
