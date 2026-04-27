package com.diy.app;

import com.diy.framework.web.HandlerMapping;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HandlerMapping mapping = new HandlerMapping();
        mapping.put("/lectures", new LectureController());
        sce.getServletContext().setAttribute("handlerMapping", mapping);
    }
}
