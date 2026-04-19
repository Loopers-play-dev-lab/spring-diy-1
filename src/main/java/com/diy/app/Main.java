package com.diy.app;

import com.diy.framework.web.HomeServlet;
import com.diy.framework.web.server.TomcatWebServer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
    public static void main(String[] args) {
//        final Tomcat tomcat = new Tomcat();
//        tomcat.setPort(8080);
        final TomcatWebServer tomcat = new TomcatWebServer();

//        try {
            tomcat.start();
//            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());

//            final Context context = tomcat.addContext("", new File(".").getAbsolutePath());
//            tomcat.addServlet(context, "homeServlet", new HomeServlet()); // 예제에서 오타있어요 Tomcat -> tomcat
//            context.addServletMappingDecoded("/home", "homeServlet");

//            awaitThread.start();
//        } catch (LifecycleException e) {
//            throw new RuntimeException(e);
//        }

    }
}
