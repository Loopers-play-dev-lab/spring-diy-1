package com.diy.config;

import com.diy.app.presentation.HomeController;
import com.diy.app.presentation.LectureAbstractController;
import com.diy.app.presentation.LectureRestController;
import com.diy.framework.web.mvc.servlet.Controller;
import com.diy.framework.web.mvc.servlet.RestController;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

    public static Map<String, Controller> controllerMapping() {
        final Map<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put("/lectures", AppConfig.lectureAbstractController());
        controllerMap.put("/home", AppConfig.homeController());

        return controllerMap;
    }

    public static Map<String,RestController> restControllerMapping() {
        final Map<String,RestController> controllerMap = new HashMap<>();
        controllerMap.put("/lecture", LectureRestController.getInstance());
        return controllerMap;
    }
    public static LectureAbstractController lectureAbstractController() {
        return LectureAbstractController.getInstance();
    }

    public static HomeController homeController() {
        return HomeController.getInstance();
    }
}
