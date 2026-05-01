package com.diy.app;

import com.diy.framework.beans.factory.BeanFactory;
import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        new BeanFactory();
        TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
