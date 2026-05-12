package com.diy.app;

import com.diy.framework.web.Controller;
import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.server.TomcatWebServer;

import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws Exception {
        BeanFactory beanFactory = new BeanFactory("com.diy.app");
        Map<String, Controller> mainControllerMap = new HashMap<>();
        mainControllerMap.put("/lectures", beanFactory.getBean(LectureController.class));
        DispatcherServlet dispatcherServlet = new DispatcherServlet(mainControllerMap);

        final TomcatWebServer tomcat = new TomcatWebServer(dispatcherServlet);
        tomcat.start();

    }
}
