package com.diy.app;

import com.diy.app.controller.api.LectureController;
import com.diy.app.controller.view.LectureListController;
import com.diy.framework.web.beans.factory.BeanContainer;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.server.controller.ControllerResolver;
import com.diy.framework.web.servlet.DispatcherServlet;

public class Main {
    public static void main(String[] args) {

        BeanContainer beanContainer = new BeanContainer(Main.class.getPackageName());
        beanContainer.init();

        ControllerResolver controllerResolver = new ControllerResolver(
            beanContainer.getBean(LectureController.class),
            beanContainer.getBean(LectureListController.class)
        );

        DispatcherServlet dispatcherServlet = new DispatcherServlet(controllerResolver);
        TomcatWebServer server = new TomcatWebServer(dispatcherServlet);
        server.start();
    }
}
