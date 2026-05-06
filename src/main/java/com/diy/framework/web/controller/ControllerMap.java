package com.diy.framework.web.controller;

import com.diy.app.controller.LectureController;
import com.diy.app.repository.build.LectureRepositoryImpl;

import java.util.Map;

public class ControllerMap {

    private final Map<String, Controller> _map = Map.of(
            "/lectures", new LectureController(new LectureRepositoryImpl())
    );

    private final Map<String, Controller> map = _map;

    public Controller find(String key) {
        if (map == null || !map.containsKey(key)) {
            throw new RuntimeException("404 Not Found");
        }
        return map.get(key);
    }
}
