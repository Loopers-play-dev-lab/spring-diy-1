package com.diy.app;

import com.diy.framework.web.mvc.FrontController;
import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        FrontController.register(new LectureController());

        final TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
