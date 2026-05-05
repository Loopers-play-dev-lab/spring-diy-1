package com.diy.framework.web.mvc;

import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {
    ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}
