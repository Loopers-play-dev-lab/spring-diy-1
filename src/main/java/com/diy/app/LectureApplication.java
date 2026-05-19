package com.diy.app;

import com.diy.framework.web.server.Controller;
import com.diy.framework.web.server.DispatcherServlet;
import com.diy.framework.web.server.TomcatWebServer;

import java.util.HashMap;
import java.util.Map;

public class LectureApplication {
    public static void main(String[] args) {
        final Map<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put("/lectures", new LectureController());

        final DispatcherServlet dispatcherServlet = new DispatcherServlet(controllerMap);
        TomcatWebServer tomcat = new TomcatWebServer(dispatcherServlet);
        tomcat.start();
    }
}
