package com.diy.app;

import com.diy.app.annotation.Application;
import com.diy.framework.web.server.TomcatWebServer;

@Application
public class Main {
    public static void main(String[] args) {
        TomcatWebServer server = new TomcatWebServer();
        server.start();
    }
}