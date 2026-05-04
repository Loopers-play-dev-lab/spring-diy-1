package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.annotations.Component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ApplicationContext {
    private static volatile ApplicationContext instance;

    private ApplicationContext() {
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ApplicationContext();
                    instance.beanScanner = new BeanScanner(BASE_PACKAGE_NAME, MODULE_PACKAGE_NAME);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private static final String BASE_PACKAGE_NAME = "com.diy.app";
    private static final String MODULE_PACKAGE_NAME = "com.diy.module";

    private final Map<String, Object> beans = new HashMap<>();

    private BeanScanner beanScanner;

    public void setBeans() throws Exception {
        List<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> clazz : classes) {
            Component component = clazz.getAnnotation(Component.class);
            if (component == null) continue;
            Object componentObject = null;
            try {
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                for (Constructor<?> constructor : constructors) {
                    if (constructor.getParameterCount() == 0) {
                        componentObject = constructor.newInstance();
                        continue;
                    }
                    componentObject = this.setArgsConstructorBean(constructor);
                }

                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces.length == 0) {
                    beans.put(clazz.getSimpleName(), componentObject);
                    System.out.println("[Component] " + clazz.getSimpleName() + " : " + componentObject);
                    continue;
                }

                for (Class<?> anInterface : interfaces) {
                    beans.put(anInterface.getSimpleName(), componentObject);
                    System.out.println("[Component] " + anInterface.getSimpleName() + " : " + componentObject);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    private Object setArgsConstructorBean(Constructor<?> argsConstructor) throws Exception {
        Class<?>[] parameterTypes = argsConstructor.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Object o = beans.get(parameterTypes[i].getSimpleName());

            if (o == null) return null;

            args[i] = o;

            System.out.println("[Parameter] " + parameterTypes[i].getSimpleName());
        }
        return argsConstructor.newInstance(args);
    }
}
