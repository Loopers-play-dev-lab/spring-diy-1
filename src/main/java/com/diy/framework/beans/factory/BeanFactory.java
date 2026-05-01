package com.diy.framework.beans.factory;

import com.diy.framework.beans.annotations.Component;

import java.util.Set;

public class BeanFactory {
    public BeanFactory() {
        BeanScanner bc = new BeanScanner("com.diy.app");
        Set<Class<?>> classes = bc.scanClassesTypeAnnotatedWith(Component.class);
        classes.forEach(System.out::println);
    }
}
