package com.diy.app;

import com.diy.framework.beans.factory.BeanStorage;
import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        BeanStorage beanStorage = new BeanStorage();
        TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
