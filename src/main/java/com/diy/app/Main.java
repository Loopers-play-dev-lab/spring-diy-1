package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;
import com.diy.framework.web.servlet.handler.mapping.ControllerHandlerMapping;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext(Main.class.getPackageName());
        applicationContext.initialize();

        ControllerHandlerMapping handlerMapping = new ControllerHandlerMapping(applicationContext);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMapping);

        TomcatWebServer webServer = new TomcatWebServer(dispatcherServlet);
        webServer.start();
    }
}
