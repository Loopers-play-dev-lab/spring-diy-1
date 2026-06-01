package com.diy.framework.web.server;

import com.diy.framework.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class TomcatStarter implements ServletContainerInitializer {
    private final ServletContextInitializer[] initializers;

    public TomcatStarter(final ServletContextInitializer[] initializers) {
        this.initializers = initializers;
    }

    @Override
    public void onStartup(final Set<Class<?>> set, final ServletContext servletContext) throws ServletException {
        for (final ServletContextInitializer initializer : this.initializers) {
            initializer.onStartup(servletContext);
        }
    }
}
