package com.diy.app;

import com.diy.framework.bean.ApplicationContext;
import com.diy.framework.controller.Controller;
import com.diy.framework.controller.RequestHandler;
import com.diy.framework.controller.RequestHandlerImpl;
import com.diy.framework.controller.RequestKey;
import com.diy.framework.servlet.DispatcherServlet;
import com.diy.framework.value.ModelAndView;
import com.diy.framework.view.ViewResolverFactory;
import com.diy.framework.view.ViewType;
import com.diy.framework.view.resolver.ViewResolver;
import com.diy.framework.web.server.TomcatWebServer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        final ApplicationContext applicationContext = new ApplicationContext(Main.class.getPackageName());
        applicationContext.initialize();

        final Map<RequestKey, RequestHandler> handlers = new HashMap<>();

        RequestHandlerImpl.handlers.forEach((key, value) ->
                handlers.put(key, ((request, response) ->
                        (ModelAndView) value.method().invoke(value.bean(), request, response)))
        );

        final Map<String, Controller> controllers = Map.of("/lectures", applicationContext.getBean(LectureController.class));
        final List<ViewResolver> viewResolvers = Stream.of(ViewType.REDIRECT, ViewType.JSP)
                .map(ViewResolverFactory::of)
                .toList();

        final DispatcherServlet servlet = new DispatcherServlet(controllers, viewResolvers);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}
