package com.diy.framework.web.controller;

import com.diy.framework.web.beans.ApplicationContext;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.beans.factory.annotation.Controller;
import com.diy.framework.web.beans.factory.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HttpServletContext {

    private final List<String> basePackages = new ArrayList<>();

    public HttpServletContext(String basePackage) {
        this.basePackages.add(ApplicationContext.class.getPackageName());
        this.basePackages.add(basePackage);
    }

    public void initialize() {
        final BeanScanner beanScanner = new BeanScanner(basePackages.get(0), basePackages.get(1));
        Set<Class<?>> controllers = beanScanner.scanClassesTypeAnnotatedWith(Controller.class);
    }

}
