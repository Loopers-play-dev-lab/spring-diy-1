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
    private Context context;

    private void setServerContext() {
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        this.context = this.tomcat.addWebapp("/", absoluteResourcesPath);

        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");

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

    public void prepareContext() {
        setServerContext();
    }

    public void addServlet(String urlPattern, String servletName, HttpServlet servlet) {
        Tomcat.addServlet(this.context, servletName, servlet);
        this.context.addServletMappingDecoded(urlPattern, servletName);
    }

    public void startServer() {
        try {
            tomcat.setPort(port);
            tomcat.start();
            Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.setContextClassLoader(getClass().getClassLoader());
            awaitThread.setDaemon(false);
            awaitThread.start();
        } catch (LifecycleException e) {
            throw new RuntimeException("톰캣 서버 실행 중 예외가 발생했습니다.", e);
        }
    }
}
