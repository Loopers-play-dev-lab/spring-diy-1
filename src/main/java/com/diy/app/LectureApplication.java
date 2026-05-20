package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

public class LectureApplication {
    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ApplicationContext(LectureApplication.class.getPackageName());
        applicationContext.initialize();
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        TomcatWebServer webServer = new TomcatWebServer(dispatcherServlet);
        webServer.start();
    }
}
