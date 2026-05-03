package com.diy.framework.web.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.CodeSource;


public class TomcatWebServer {

    private final Tomcat tomcat = new Tomcat();
    private final int port = 8080;
    private final HttpServlet dispatcherServlet;

    public TomcatWebServer(HttpServlet dispatcherServlet) {
        this.dispatcherServlet = dispatcherServlet;
    }

    public void start() {
        setServerContext();
        startServerInternal();
        overrideRootMapping();

        for (var child : ((org.apache.catalina.Context) tomcat.getHost().findChildren()[0]).findServletMappings()) {
            System.out.println("[mapping] " + child + " -> "
                    + ((org.apache.catalina.Context) tomcat.getHost().findChildren()[0]).findServletMapping(child));
        }

        startDaemonAwaitThread();
    }

    public void startServerInternal() {
        try {
            tomcat.setPort(port);
            tomcat.start();
            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.start();
        } catch (LifecycleException e) {
            throw new RuntimeException("톰켓 서버 실행 중 예외가 발생했습니다.", e);
        }
    }

    private void setServerContext() {
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        final Context context = this.tomcat.addWebapp("/", absoluteResourcesPath);

        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");

        Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet);

        context.addServletMappingDecoded("/", "dispatcherServlet");

        setServerResources(context);
    }

    private void setServerResources(final Context context) {
        final String classPath = getClassPath();

        final StandardRoot resources = new StandardRoot(context);
        resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", classPath, "/"));

        context.setResources(resources);
    }

    private String getClassPath() {
        try {
            final CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();

            return new File(codeSource.getLocation().toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void startDaemonAwaitThread() {
        final Thread awaitThread = new Thread(() -> TomcatWebServer.this.tomcat.getServer().await());
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    private void overrideRootMapping() {
        Context context = (Context) tomcat.getHost().findChildren()[0];
        context.addServletMappingDecoded("/", "dispatcherServlet");
    }
}
