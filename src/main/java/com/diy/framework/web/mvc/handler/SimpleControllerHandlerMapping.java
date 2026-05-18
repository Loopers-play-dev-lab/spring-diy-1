package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.controller.Controller;

import java.util.HashMap;
import java.util.Map;

public class SimpleControllerHandlerMapping implements HandlerMapping {
    private final Map<String, Controller> controllers = new HashMap<>();

    public void put(String key, Controller controller) {
        controllers.put(key, controller);
    }

    @Override
    public Object getHandler(String key) {
        return controllers.get(key);
    }
}
