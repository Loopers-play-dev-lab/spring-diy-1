package com.diy.app;

import com.diy.framework.web.HomeServlet;
import com.diy.framework.web.server.TomcatWebServer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        final TomcatWebServer tomcat = new TomcatWebServer();
        tomcat.start();
    }
}
