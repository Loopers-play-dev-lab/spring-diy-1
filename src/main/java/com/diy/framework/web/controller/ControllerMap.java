package com.diy.framework.web.controller;

import com.diy.app.controller.LectureController;

import java.util.Map;

public class ControllerMap {

    // TODO: 이거 컨트롤러 만들 때마다 알아서 넣어지게 못하나?
    private static final Map<String, Controller> _map = Map.of(
            "/lectures", new LectureController()
    );;

    private static Map<String, Controller> map = _map;

    public static Controller find(String key) {
        return map.get(key);
    }
}
