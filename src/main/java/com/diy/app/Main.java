package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext(Main.class.getPackageName());
        applicationContext.initialize();

        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext.getHandlerMap());
        TomcatWebServer webServer = new TomcatWebServer(dispatcherServlet);
        webServer.start();
    }
}
