package com.diy.app.controller;

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
