package com.diy.framework.web.server;

import com.diy.framework.web.servlet.ServletContextInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.CodeSource;

public class TomcatWebServer implements WebServer {

    private final Tomcat tomcat = new Tomcat();
    private final int port = 8080;
    private final ServletContextInitializer[] initializers;

    public TomcatWebServer(ServletContextInitializer... initializers) {
        this.initializers = initializers;
    }

    @Override
    public void start() {
        setServerContext();
        startServerInternal();
    }

    @Override
    public void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new WebServerException("톰켓 서버 종료 중 예외가 발생했습니다.", e);
        }
    }

    private void setServerContext() {
        String resourcesPath = Paths.get("src", "main", "resources").toString();
        String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        Context context = this.tomcat.addWebapp("/", absoluteResourcesPath);
        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");

        setServerResources(context);
        TomcatStarter starter = new TomcatStarter(initializers);
        context.addServletContainerInitializer(starter, null);
    }

    private void startServerInternal() {
        try {
            tomcat.setPort(port);
            tomcat.start();
            Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.setContextClassLoader(getClass().getClassLoader());
            awaitThread.setDaemon(false);
            awaitThread.start();
        } catch (LifecycleException e) {
            throw new WebServerException("톰켓 서버 실행 중 예외가 발생했습니다.", e);
        }
    }

    private void setServerResources(Context context) {
        String classPath = getClassPath();
        StandardRoot resources = new StandardRoot(context);
        resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", classPath, "/"));
        context.setResources(resources);
    }

    private String getClassPath() {
        try {
            CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
            return new File(codeSource.getLocation().toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
