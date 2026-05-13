package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

public class LectureApplication {
    public static void main(String[] args) {
        final ApplicationContext ac = new ApplicationContext(LectureApplication.class.getPackageName());
        ac.initialize();

        final DispatcherServlet servlet = new DispatcherServlet(ac.getControllerMapping());
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}
