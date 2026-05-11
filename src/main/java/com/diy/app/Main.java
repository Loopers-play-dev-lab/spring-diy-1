package com.diy.app;

import com.diy.framework.controller.Controller;
import com.diy.framework.servlet.DispatcherServlet;
import com.diy.framework.view.ViewResolverFactory;
import com.diy.framework.view.ViewType;
import com.diy.framework.view.resolver.ViewResolver;
import com.diy.framework.web.server.TomcatWebServer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Map<String, Controller> controllers = Map.of("/lectures", new LectureController());
        List<ViewResolver> viewResolvers = Stream.of(ViewType.REDIRECT, ViewType.JSP)
                .map(ViewResolverFactory::of)
                .toList();
        DispatcherServlet servlet = new DispatcherServlet(controllers, viewResolvers);
        TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}
