package com.diy.framework.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@FunctionalInterface
public interface ServletContextInitializer {
    void onStartup(final ServletContext servletContext) throws ServletException;
}
