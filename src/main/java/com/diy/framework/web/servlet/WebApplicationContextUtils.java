package com.diy.framework.web.servlet;

import com.diy.framework.context.ApplicationContext;
import javax.servlet.ServletContext;

public abstract class WebApplicationContextUtils {

    public static ApplicationContext getApplicationContext(ServletContext servletContext) {
        return (ApplicationContext) servletContext.getAttribute(ApplicationContext.APPLICATION_CONTEXT_ATTRIBUTE);
    }
}
