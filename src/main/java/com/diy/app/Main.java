package com.diy.app;

import com.diy.app.lecture.LectureController;
import com.diy.framework.Controller;
import com.diy.framework.DispatcherServlet;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.server.TomcatWebServer;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory("com.diy.app");
        LectureController lectureController = beanFactory.getBean(LectureController.class);

        Map<String, Controller> controllerMapping = new HashMap<>();
        controllerMapping.put("/lectures", lectureController);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(controllerMapping);
        TomcatWebServer webServer = new TomcatWebServer(dispatcherServlet);
        webServer.start();
    }
}
