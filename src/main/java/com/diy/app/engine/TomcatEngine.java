package com.diy.app.engine;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class TomcatEngine {
    private final Tomcat tomcat;
    private final ManualApplicationContext applicationContext;

    public TomcatEngine(Tomcat tomcat) {
        this.tomcat = tomcat;
        this.applicationContext = ManualApplicationContext.init();
    }

    public void startEngine() {
        tomcat.setPort(8080);

        try{
            tomcat.start();
            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());

            final Context context = tomcat.addWebapp("", new File("src/main/resources").getAbsolutePath());

            applicationContext.getServletMapper().getServletList().forEach(servlet -> {
                Tomcat.addServlet(context, servlet.servletName(), servlet.servlet());
                context.addServletMappingDecoded(servlet.path(), servlet.servletName());
            });

            awaitThread.start();
        } catch (LifecycleException exception) {
            throw new RuntimeException(exception);
        }
    }
}


