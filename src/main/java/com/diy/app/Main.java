package com.diy.app;

import com.diy.framework.web.Controller;
import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;

import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ApplicationContext("com.diy.app");
        Map<String, Controller> mainControllerMap = new HashMap<>();
        mainControllerMap.put("/lectures", applicationContext.getBean(LectureController.class));
        System.out.println("beans : " + applicationContext.getBean("ObjectMapper"));
        DispatcherServlet dispatcherServlet = new DispatcherServlet(mainControllerMap);

        final TomcatWebServer tomcat = new TomcatWebServer(dispatcherServlet);
        tomcat.start();

    }
}
