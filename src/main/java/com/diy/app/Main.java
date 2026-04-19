package com.diy.app;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        try {
            final Context context = tomcat.addWebapp("", new File("src/main/resources").getAbsolutePath());
            Tomcat.addServlet(context, "lectureServlet", new LectureServlet());
            context.addServletMappingDecoded("/lecture", "lectureServlet");

            tomcat.start();

            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.start();

        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
