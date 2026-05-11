package com.diy.app;

import com.diy.framework.web.annotations.Component;
import com.diy.framework.web.beans.factory.BeanContainer;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

import java.util.HashMap;
import java.util.Map;

@Component
public class Application {
    public static void main(String[] args) {
        final Map<String, Controller> controllerMapping = new HashMap<>();
        BeanContainer beanContainer = new BeanContainer("com.diy.app");
        controllerMapping.put("/lectures", beanContainer.getBean(LectureController.class));

        final DispatcherServlet servlet = new DispatcherServlet(controllerMapping);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}
