package com.diy.framework.web.servlet.handler.mapping;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);
}
