package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

public class Main {

    public static void main(String[] args) {
        TomcatWebServer server = new TomcatWebServer();
        server.start();
    }
}
