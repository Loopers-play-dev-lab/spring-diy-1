package com.diy.app;

import com.diy.framework.web.mvc.*;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

import java.util.*;

public class LectureApplication {
    public static void main(String[] args) {
        Map<String, Object> beanContainer = new HashMap<>();

        BeanFactory factory = new BeanFactory();
        factory.initialize("com.diy.app");
        factory.printBeanContainer();

        final Map<String, Controller> controllerMapping = new HashMap<>();
        controllerMapping.put("/lectures", new LectureController());

        final DispatcherServlet servlet = new DispatcherServlet(controllerMapping);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }

}
