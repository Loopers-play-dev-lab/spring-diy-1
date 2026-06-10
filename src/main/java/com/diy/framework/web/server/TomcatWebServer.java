package com.diy.framework.web.server;

import com.diy.framework.web.servlet.DispatcherServlet;
import com.diy.framework.web.servlet.ServletContextInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.Servlet;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.logging.Level;


public class TomcatWebServer implements WebServer {

    private final ServletContextInitializer[] initializers;
    private final Servlet dispatcherServlet = new DispatcherServlet();
    private final Tomcat tomcat = new Tomcat();
    private final int port = 8080;
    private final Object monitor = new Object();
    private boolean started = false;

    public TomcatWebServer(final ServletContextInitializer... initializers) {
        this.initializers = initializers;
    }

    @Override
    public void start() throws WebServerException {
        synchronized (this.monitor) {
            if (this.started) {
                return;
            }

            try {
                initialize();
                this.tomcat.setPort(port);
                this.tomcat.start();
            } catch (LifecycleException e) {
                throw new WebServerException("Unable to start embedded Tomcat", e);
            }
        }
    }

    @Override
    public void stop() {
        synchronized (this.monitor) {
            try {
                this.started = false;
                this.tomcat.stop();
                this.tomcat.destroy();
            } catch (LifecycleException e) {
                throw new WebServerException("stop failed.", e);
            }
        }
    }

    private void initialize() {
        this.started = true;
        offTomcatLogger();
        setServerContext();
        startDaemonAwaitThread();
    }

    private void offTomcatLogger() {
        java.util.logging.Logger tomcatCoreLogger = java.util.logging.Logger.getLogger("org.apache");
        tomcatCoreLogger.setLevel(Level.OFF);
    }

    private void setServerContext() {
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        final Context context = this.tomcat.addWebapp("/", absoluteResourcesPath);

        context.addServletContainerInitializer(new TomcatStarter(this.initializers), null);
        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");

        setServerResources(context);
        setDispatcherServlet(context);
    }

    private void setDispatcherServlet(final Context context) {
        final Wrapper sw = this.tomcat.addServlet(context.getPath(), "dispatcherServlet", dispatcherServlet);
        sw.addMapping("/");
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
}
