package com.diy.framework.web;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private final Map<String, Controller> mappings = new HashMap<>();

    public void put(String url, Controller controller) {
        mappings.put(url, controller);
    }

    public Controller getHandler(String url) {
        return mappings.get(url);
    }
}
