package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SimpleUrlHandlerMapping implements HandlerMapping {

    private final Map<String, Controller> handlerMap;

    public SimpleUrlHandlerMapping(final Map<String, Controller> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerMap.get(request.getRequestURI());
    }
}
