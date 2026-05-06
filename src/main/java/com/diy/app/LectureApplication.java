package com.diy.app;

import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

import java.util.HashMap;
import java.util.Map;

public class LectureApplication {
    public static void main(String[] args) {
        final BeanFactory beanFactory = new BeanFactory("com.diy.app");
        final LectureController lectureController = beanFactory.getBean(LectureController.class);

        final Map<String, Controller> controllerMapping = new HashMap<>();
        controllerMapping.put("/lectures", lectureController);

        final DispatcherServlet servlet = new DispatcherServlet(controllerMapping);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}
