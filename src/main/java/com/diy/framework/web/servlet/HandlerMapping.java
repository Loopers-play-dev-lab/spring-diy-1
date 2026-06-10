package com.diy.framework.web.servlet;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    Object getHandler(final HttpServletRequest req) throws Exception;
}
