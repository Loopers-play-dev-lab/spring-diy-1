package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.RequestMappingHandlerMapping;
import com.diy.framework.web.server.TomcatWebServer;


public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ApplicationContext("com.diy.app");
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping(applicationContext.getBeans());
        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMapping);

        final TomcatWebServer tomcat = new TomcatWebServer(dispatcherServlet);
        tomcat.start();

    }
}
