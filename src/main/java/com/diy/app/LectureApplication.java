package com.diy.app;

import com.diy.framework.web.annotation.Component;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureApplication {
    public static void main(String[] args) {
        final Map<String, Controller> controllerMapping = new HashMap<>();
        controllerMapping.put("/lectures", new LectureController());

        final List<Class<? extends Annotation>> annotations = new ArrayList<>();
        annotations.add(Component.class);

        final DispatcherServlet servlet = new DispatcherServlet(controllerMapping);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);

        tomcatWebServer.scanBean(annotations,"com.diy.app");
        tomcatWebServer.start();
    }
}
