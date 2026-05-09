package com.diy.app;

import com.diy.framework.web.context.BeanContext;
import com.diy.framework.web.server.TomcatWebServer;

public class Main {

    public static void main(String[] args) {

        new BeanContext(Main.class.getPackage().getName());
        TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
