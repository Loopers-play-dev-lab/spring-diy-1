package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        final TomcatWebServer tomcat = new TomcatWebServer();
        tomcat.start();
    }
}
