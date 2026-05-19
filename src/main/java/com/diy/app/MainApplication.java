package com.diy.app;

import com.diy.app.controller.LectureController;
import com.diy.framework.web.beans.ApplicationContext;
import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.controller.ControllerMap;
import com.diy.framework.web.server.TomcatWebServer;

public class MainApplication {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new ApplicationContext(MainApplication.class.getPackageName());

        Controller lectureController = (Controller) applicationContext.findBean(LectureController.class.getName());
        ControllerMap controllerMap = new ControllerMap();
        controllerMap.put("/lecture", lectureController);

        final TomcatWebServer tomcat = new TomcatWebServer();
        tomcat.start();
    }
}
