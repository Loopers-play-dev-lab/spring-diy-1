package com.diy.app;

import com.diy.config.AppConfig;
import com.diy.framework.web.mvc.servlet.Controller;
import com.diy.framework.web.mvc.servlet.DispatcherServlet;
import com.diy.framework.web.server.TomcatWebServer;

import java.util.HashMap;
import java.util.Map;

public class LectureApplication {
    public static void main(String[] args) {
        Map<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put("/lectures", AppConfig.lectureAbstractController());
        DispatcherServlet dispatcherServlet = new DispatcherServlet(controllerMap);

        final TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
