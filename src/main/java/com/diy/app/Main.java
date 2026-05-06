package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        TomcatWebServer tomcat = new TomcatWebServer();
        tomcat.start();
    }
}
