package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

public class LectureApplication {
    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ApplicationContext(LectureApplication.class.getPackageName());
        applicationContext.initialize();

        final DispatcherServlet servlet = new DispatcherServlet(applicationContext.getControllerRegistry());
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}
