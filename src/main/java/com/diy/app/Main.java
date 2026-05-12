package com.diy.app;

import com.diy.app.lecture.LectureController;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.servlet.DispatcherServlet;
import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext("com.diy.app");
        LectureController lectureController = applicationContext.getBean(LectureController.class);

        Map<String, Controller> controllerMapping = new HashMap<>();
        controllerMapping.put("/lectures", lectureController);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(controllerMapping);
        TomcatWebServer webServer = new TomcatWebServer(dispatcherServlet);
        webServer.start();
    }
}
