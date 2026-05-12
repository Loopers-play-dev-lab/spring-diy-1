package com.diy.app;

import com.diy.framework.web.beans.Autowired;
import com.diy.framework.web.beans.Component;
import com.diy.framework.web.beans.factory.BeanContainer;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.server.TomcatWebServer;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        BeanScanner beanScanner = new BeanScanner("com.diy.app");
        Set<Class<?>> components = new HashSet<>(beanScanner.scanClassesTypeAnnotatedWith(Component.class));
        BeanContainer.register(components);

        Set<Constructor> autowiredConstructors = beanScanner.scanConstructorsAnnotatedWith(Autowired.class);
        BeanContainer.inject(autowiredConstructors);

        final TomcatWebServer tomcat = new TomcatWebServer();
        tomcat.start();
    }
}
