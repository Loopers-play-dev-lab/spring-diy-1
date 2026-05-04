package com.diy.app;

import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory("com.diy.app");

        TomcatWebServer webServer = new TomcatWebServer();
        webServer.start();
    }
}
