package com.diy.app.utils;

import com.diy.app.annotation.Autowired;
import com.diy.app.annotation.Component;

import java.lang.reflect.Field;
import java.util.Set;

public class ApplicationContext {
    private static BeanFactory beanFactory;


    public static void run(Class<?> main) {
        final String basePackage = main.getPackage().getName();
        BeanScanner beanScanner = new BeanScanner(basePackage);
        BeanFactory factory = new BeanFactory();
        DependencyInjector injector = new DependencyInjector(factory);

        // 1. 클래스 스캔 및 빈 생성
        Set<Class<?>> componentClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        try {
            for (Class<?> clazz : componentClasses) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                factory.registerBean(clazz.getName(), instance);
                for (Class<?> iface : clazz.getInterfaces()) {
                    // 인터페이스 이름으로 등록 (com.diy.app.repo.ILectureRepository)
                    factory.registerBean(iface.getName(), instance);
                }
            }
            ApplicationContext.beanFactory = factory;
            // 2. 필드 스캔 및 의존성 주입
            Set<Field> autowiredFields = beanScanner.scanFieldTypeAnnotatedWith(Autowired.class);
            injector.inject(autowiredFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getBean(String name) {
        if (beanFactory == null) {
            throw new IllegalStateException("ApplicationContext가 초기화되지 않았습니다. run()을 먼저 호출하세요.");
        }
        return beanFactory.getBean(name);
    }
}
