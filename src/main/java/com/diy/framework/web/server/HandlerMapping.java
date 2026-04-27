package com.diy.framework.web.server;

import com.diy.app.controller.AbstractController;

import java.util.Map;

public class HandlerMapping {
    private final Map<HandlerContext, AbstractController> map;

    public HandlerMapping(Map<HandlerContext, AbstractController> map) {
        this.map = map;
    }

    public AbstractController getController(HandlerContext handlerContext) {
        if (!map.containsKey(handlerContext)) {
            throw new IllegalArgumentException("wrong context");
        }
        return map.get(handlerContext);
    }
}
