package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class SimpleUrlHandlerMapping implements HandlerMapping {

    private final Map<String, Controller> handlerMap = new HashMap<>();

    public void initialize(final Map<String, Object> beans) {
        beans.forEach((beanName, bean) -> {
            if (bean instanceof Controller) {
                handlerMap.put(beanName, (Controller) bean);
            }
        });
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerMap.get(request.getRequestURI());
    }
}