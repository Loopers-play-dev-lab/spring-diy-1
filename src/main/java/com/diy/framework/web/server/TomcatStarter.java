package com.diy.framework.web.server;

import com.diy.framework.web.servlet.ServletContextInitializer;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class TomcatStarter implements ServletContainerInitializer {

    private final ServletContextInitializer[] initializers;

    public TomcatStarter(ServletContextInitializer... initializers) {
        this.initializers = initializers;
    }

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        for (ServletContextInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }
    }
}
