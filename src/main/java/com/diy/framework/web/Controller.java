package com.diy.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {
    void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}
