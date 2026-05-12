package com.diy.framework.web.controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerMap {

    private static Map<String, Controller> _map = new HashMap<>();

    private final Map<String, Controller> map = _map;

    public void put(String path, Controller controller) {
        _map.put(path, controller);
    }

    public Controller find(String key) {
        if (map == null || !map.containsKey(key)) {
            throw new RuntimeException("404 Not Found");
        }
        return map.get(key);
    }
}
