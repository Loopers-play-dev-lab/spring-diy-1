package com.diy.app;

import com.diy.app.lecture.LectureController;
import com.diy.framework.web.mvc.HandlerMapping;
import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        HandlerMapping.getInstance().register("/lectures", new LectureController());

        TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
