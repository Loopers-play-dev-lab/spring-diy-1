package com.diy.app;

import com.diy.framework.web.beans.annotations.Component;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LectureApplication {
    public static void main(String[] args)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        BeanScanner beanScanner = new BeanScanner("com.diy.app");
        Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        for (Class<?> clazz : classes) {

            if(clazz.equals(LectureRepository.class)) {
                LectureRepository lectureRepository = (LectureRepository) clazz.getDeclaredConstructor().newInstance();
            }
            System.out.println(clazz.getName());
        }

        final Map<String, Controller> controllerMapping = new HashMap<>();
        controllerMapping.put("/lectures", new LectureController());

        final DispatcherServlet servlet = new DispatcherServlet(controllerMapping);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
