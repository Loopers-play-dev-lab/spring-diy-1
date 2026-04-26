package com.diy.app.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {
    void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
