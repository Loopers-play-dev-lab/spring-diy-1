package com.diy.app;

import com.diy.framework.web.annotation.Component;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LectureApplication {

    public static void main(String[] args) throws Exception {
        final BeanScanner beanScanner = new BeanScanner("com.diy.app");
        final Set<Class<?>> beanClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        final BeanFactory beanFactory = new BeanFactory();
        beanFactory.initialize(beanClasses);

        final LectureService lectureService = (LectureService) beanFactory.getBean(LectureService.class);

        final Map<String, Controller> controllerMapping = new HashMap<>();
        controllerMapping.put("/lectures", new LectureController(lectureService));

        final DispatcherServlet servlet = new DispatcherServlet(controllerMapping);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}