package com.diy.app.controller;

import com.diy.app.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {
    ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}