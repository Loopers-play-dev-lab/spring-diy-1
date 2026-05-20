package com.diy.app;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.HandlerAdapter;
import com.diy.framework.web.mvc.HandlerMapping;
import com.diy.framework.web.mvc.adapter.AnnotationHandlerAdapter;
import com.diy.framework.web.mvc.adapter.SimpleControllerHandlerAdapter;
import com.diy.framework.web.mvc.handler.AnnotationHandlerMapping;
import com.diy.framework.web.mvc.handler.SimpleUrlHandlerMapping;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.mvc.view.ViewResolver;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

import java.util.List;

public class LectureApplication {
    public static void main(String[] args) {
        final ApplicationContext ac = new ApplicationContext(LectureApplication.class.getPackageName());
        ac.initialize();

        final AnnotationHandlerMapping annotationHM = new AnnotationHandlerMapping();
        annotationHM.initialize(ac.getBeans());

        final SimpleUrlHandlerMapping simpleHM = new SimpleUrlHandlerMapping(ac.getControllerMapping());

        final List<HandlerMapping> handlerMappings = List.of(annotationHM, simpleHM);
        final List<HandlerAdapter> handlerAdapters = List.of(new AnnotationHandlerAdapter(), new SimpleControllerHandlerAdapter());
        final List<ViewResolver> viewResolvers = List.of(new UrlBasedViewResolver(), new JspViewResolver());

        final DispatcherServlet servlet = new DispatcherServlet(handlerMappings, handlerAdapters, viewResolvers);
        new TomcatWebServer(servlet).start();
    }
}
