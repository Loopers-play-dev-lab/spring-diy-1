package com.diy.app.ctrl;

import com.diy.app.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {
    ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
