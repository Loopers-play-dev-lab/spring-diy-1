package com.diy.framework.web.mvc.controller;

import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ControllerDefinition {
    ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception;
    String getUrl();
}
