package com.diy.app.utils;

import com.diy.app.annotation.Autowired;
import com.diy.app.annotation.Bean;
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

        ApplicationContext.beanFactory = factory;

        // 1. 빈 생성 대상 스캔
        Set<Class<?>> components = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        Set<Constructor<?>> constructors = beanScanner.scanConstructorTypeAnnotatedWith(Autowired.class);
        Set<Field> fields = beanScanner.scanFieldTypeAnnotatedWith(Autowired.class);
        Set<Method> methods = beanScanner.scanMethodTypeAnnotatedWith(Bean.class);

        // 2. 팩토리에게 빈 생성 위임
        factory.createBean(components,constructors, injector);

        // 3. Configuration의 빈 메서드 실행
        factory.createBeanMethods(methods, injector);

        // 4. 필드 스캔 및 의존성 주입
        injector.fieldInject(fields);
    }

    public static <T> T getBean(Class<T> type) {
        if (beanFactory == null) {
            throw new IllegalStateException("ApplicationContext가 초기화되지 않았습니다. run()을 먼저 호출하세요.");
        }
        return (T) beanFactory.getBeanByType(type);
    }
}
