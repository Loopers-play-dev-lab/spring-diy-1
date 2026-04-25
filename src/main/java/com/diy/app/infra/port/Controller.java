package com.diy.app.infra.port;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {
    public static final String PREFIX_URL = "";
    void handleRequest(final HttpServletRequest req, final HttpServletResponse resp) throws Exception;
}
