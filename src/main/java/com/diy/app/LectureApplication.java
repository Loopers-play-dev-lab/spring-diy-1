package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

import java.util.Map;

public class LectureApplication {
    public static void main(String[] args) {
        final ApplicationContext ac = new ApplicationContext(LectureApplication.class.getPackageName());
        ac.initialize();

        final Map<String, Controller> controllerMapping = ac.getControllerMapping();
        final DispatcherServlet servlet = new DispatcherServlet(controllerMapping);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}
