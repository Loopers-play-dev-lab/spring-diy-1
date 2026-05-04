package com.diy.app;

import com.diy.framework.bean.Autowired;
import com.diy.framework.bean.BeanScanner;
import com.diy.framework.bean.Repository;
import com.diy.framework.controller.Controller;
import com.diy.framework.servlet.DispatcherServlet;
import com.diy.framework.web.server.TomcatWebServer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        BeanScanner beanScanner = new BeanScanner("com.diy.app");


        // 프레임 워크 위임 필수
        var beanClasses = beanScanner.scanClassesTypeAnnotatedWith(Repository.class);

        Map<Class<?>, Object> beans = new HashMap<>();
        for (Class<?> clazz : beanClasses) {
            beans.put(clazz, clazz.getDeclaredConstructor().newInstance());
        }

        LectureController lectureController = new LectureController();
        beans.put(LectureController.class, lectureController);

        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(bean, beans.get(field.getType()));
                }
            }
        }

        Map<String, Controller> controllers = Map.of("/lectures", lectureController);
        final TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
