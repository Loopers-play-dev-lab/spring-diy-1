package com.diy.framework.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {

    void handleRequest(final HttpServletRequest req, final HttpServletResponse rep) throws Exception;
}
