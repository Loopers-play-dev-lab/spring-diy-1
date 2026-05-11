package com.diy.app.utils;

import com.diy.app.annotation.Autowired;
import com.diy.app.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class ApplicationContext {
    private static BeanFactory beanFactory;


    public static void run(Class<?> main) {
        final String basePackage = main.getPackage().getName();
        BeanScanner beanScanner = new BeanScanner(basePackage);
        BeanFactory factory = new BeanFactory();
        DependencyInjector injector = new DependencyInjector(factory);

        // 1. 컴포넌트 스캔 및 빈 생성 및 등록
        Set<Class<?>> componentClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        factory.createBean(componentClasses);
        ApplicationContext.beanFactory = factory;
        // 2. 생성자 스캔 및 의존성 주입
        Set<Constructor<?>> constructors = beanScanner.scanConstructorTypeAnnotatedWith(Autowired.class);
        injector.constructorInject(constructors);
        ApplicationContext.beanFactory = factory;
        // 3. 필드 스캔 및 의존성 주입
        Set<Field> fields = beanScanner.scanFieldTypeAnnotatedWith(Autowired.class);
        injector.fieldInject(fields);
        ApplicationContext.beanFactory = factory;
    }

    public static Object getBean(String name) {
        if (beanFactory == null) {
            throw new IllegalStateException("ApplicationContext가 초기화되지 않았습니다. run()을 먼저 호출하세요.");
        }
        return beanFactory.getBean(name);
    }
}
