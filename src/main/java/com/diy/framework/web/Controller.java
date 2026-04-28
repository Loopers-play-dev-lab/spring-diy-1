package com.diy.framework.web;

import com.diy.framework.web.mvc.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {
    String handleRequest(final HttpServletRequest request, final HttpServletResponse response, final Model model) throws Exception;
}
