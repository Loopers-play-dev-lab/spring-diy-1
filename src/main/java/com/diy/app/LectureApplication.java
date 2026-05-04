package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

public class LectureApplication {
    public static void main(String[] args) {
        final TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
