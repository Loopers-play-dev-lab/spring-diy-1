package com.diy.framework.web.controller;

import com.diy.app.controller.LectureController;
import com.diy.app.repository.LectureRepository;

import java.util.Map;

public class ControllerMap {

    // TODO: 이거 컨트롤러 만들 때마다 알아서 넣어지게 못하나?
    private final Map<String, Controller> _map = Map.of(
            "/lectures", new LectureController(new LectureRepository())
    );

    private final Map<String, Controller> map = _map;

    public Controller find(String key) {
        if (map == null || !map.containsKey(key)) {
            throw new RuntimeException("404 Not Found");
        }
        return map.get(key);
    }
}
