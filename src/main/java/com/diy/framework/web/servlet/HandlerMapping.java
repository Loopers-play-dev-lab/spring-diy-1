package com.diy.framework.web.servlet;

import com.diy.framework.web.mvc.Controller;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private final Map<String, Controller> handlerMap = new HashMap<>();

    public void register(String path, Controller controller) {
        handlerMap.put(path, controller);
    }

    public Controller getHandler(String path) {
        return handlerMap.get(path);
    }
}
