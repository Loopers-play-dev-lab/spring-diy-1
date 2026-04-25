package com.diy.app.infra.servlet;

import com.diy.app.business.controller.LectureController;
import com.diy.app.business.service.LectureService;
import com.diy.app.infra.port.Controller;
import com.diy.app.infra.viewRender.ViewResolver;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class UrlControllerMapper {
    private final Map<String, Controller> uriToController = new TreeMap<>(Comparator.comparingInt(String::length).reversed().thenComparing(String::compareTo));
    private static UrlControllerMapper instance;

    public UrlControllerMapper() {
        uriToController.put("/lectures", new LectureController(LectureService.getInstance()));
    }

    public Controller findController(String uri) {
        for (String k : uriToController.keySet()) {
            if (uri.startsWith(k)) return uriToController.get(k);
        }
        throw new IllegalArgumentException("해당 URI와 매칭되는 컨트롤러가 존재하지 않습니다.");
    }

    public static UrlControllerMapper getInstance() {
        if (instance == null) instance = new UrlControllerMapper();
        return instance;
    }
}
