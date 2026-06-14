package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.server.WebServer;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext(Main.class.getPackageName());
        applicationContext.initialize();
        WebServer webServer = applicationContext.createWebServer();
        webServer.start();
    }
}
