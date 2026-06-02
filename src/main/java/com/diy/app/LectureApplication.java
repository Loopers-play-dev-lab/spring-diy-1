package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

public class LectureApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext(LectureApplication.class.getPackageName());
        applicationContext.initialize();

        DispatcherServlet servlet = new DispatcherServlet();
        TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}
