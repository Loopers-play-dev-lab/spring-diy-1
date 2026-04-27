package com.diy.framework.web.server;

import com.diy.framework.web.controller.AbstractController;

import java.util.Map;

public class HandlerMapping {
    private final Map<String, AbstractController> map;

    public HandlerMapping(Map<String, AbstractController> map) {
        this.map = map;
    }

    public AbstractController getController(String path) {
        if (!map.containsKey(path)) {
            throw new IllegalArgumentException("wrong context");
        }
        return map.get(path);
    }
}
