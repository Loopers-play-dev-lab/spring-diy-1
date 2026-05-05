package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

public class LectureApplication {
    public static void main(String[] args) {
        DispatcherServlet servlet = new DispatcherServlet();
        TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
