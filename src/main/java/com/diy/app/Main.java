package com.diy.app;

import com.diy.framework.web.Controller;
import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.server.TomcatWebServer;

import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        Map<String, Controller> mainControllerMap = new HashMap<>();
        mainControllerMap.put("/lectures", new LectureController());
        DispatcherServlet dispatcherServlet = new DispatcherServlet(mainControllerMap);

        final TomcatWebServer tomcat = new TomcatWebServer(dispatcherServlet);
        tomcat.start();

    }
}
