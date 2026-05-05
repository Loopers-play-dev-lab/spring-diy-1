package com.diy.app;

import com.diy.framework.web.beans.annotations.Autowired;
import com.diy.framework.web.beans.annotations.Component;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LectureApplication {
    public static void main(String[] args)
        throws InvocationTargetException, InstantiationException, IllegalAccessException {
        BeanScanner beanScanner = new BeanScanner("com.diy.app");

        Map<Class<?>, Object> map = new HashMap<>();

        Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        for (Class<?> clazz : classes) {
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : declaredConstructors) {
                if(!constructor.isAnnotationPresent(Autowired.class)) {
                    map.put(clazz, constructor.newInstance());
                }
            }
        }

        for (Class<?> clazz : classes) {
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : declaredConstructors) {
                if(constructor.isAnnotationPresent(Autowired.class)) {
                    List<Object> list = new ArrayList<>();
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    for (Class<?> parameterType : parameterTypes) {
                        list.add(map.get(parameterType));
                    }
                    map.put(clazz, constructor.newInstance(list.toArray()));
                }
            }
        }

        final Map<String, Controller> controllerMapping = new HashMap<>();
        controllerMapping.put("/lectures", new LectureController());

        final DispatcherServlet servlet = new DispatcherServlet(controllerMapping);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
