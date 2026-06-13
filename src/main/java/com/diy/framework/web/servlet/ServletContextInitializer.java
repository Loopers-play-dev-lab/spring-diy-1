package com.diy.framework.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public interface ServletContextInitializer {

    void onStartup(ServletContext servletContext) throws ServletException;
}
