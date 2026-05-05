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

        // 1. 클래스 스캔 및 빈 생성
        Set<Class<?>> componentClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        try {
            for (Class<?> clazz : componentClasses) {

                // @Autowired가 없는 클래스 먼저 생성
                boolean hasAutowired = Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> field.isAnnotationPresent(Autowired.class));
                if(!hasAutowired) {
                    try {
                        Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
                        defaultConstructor.setAccessible(true);
                        Object instance = defaultConstructor.newInstance();
                        factory.registerBean(clazz.getName(), instance);
                        for (Class<?> iface : clazz.getInterfaces()) {
                            // 인터페이스 이름으로 등록 (com.diy.app.repo.ILectureRepository)
                            factory.registerBean(iface.getName(), instance);
                        }
                        defaultConstructor.setAccessible(false);
                    } catch (NoSuchMethodException e) {
                        System.out.println("기본 생성자 없음, 생성자 주입 대기중: "+clazz.getName());
                    }
                }
            }
            ApplicationContext.beanFactory = factory;
            // 2. 생성자 스캔 및 의존성 주입
            Set<Constructor<?>> constructors = beanScanner.scanConstructorTypeAnnotatedWith(Autowired.class);
            for(Constructor<?> constructor : constructors) {
                Class<?> clazz = constructor.getDeclaringClass();

                // 생성자 파라미터 타입 조회
                Object[] args = Arrays.stream(constructor.getParameterTypes())
                        .map(type -> factory.getBean(type.getName()))
                        .toArray();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance(args);
                factory.registerBean(clazz.getName(), instance);
                for(Class<?> iface : clazz.getInterfaces()) {
                    factory.registerBean(iface.getName(), instance);
                }
                constructor.setAccessible(false);
            }
            ApplicationContext.beanFactory = factory;

//            Set<Field> autowiredFields = beanScanner.scanFieldTypeAnnotatedWith(Autowired.class);
//            injector.inject(autowiredFields);
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
