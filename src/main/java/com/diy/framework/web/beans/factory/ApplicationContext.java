package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.annotations.Autowired;
import com.diy.framework.web.beans.annotations.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

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

    private final Map<String, Object> beans = new ConcurrentHashMap<>();
    private Set<Class<?>> classSet = new HashSet<>();
    private final Map<String, Class<?>> beanInterfaces = new HashMap<>();

    private BeanScanner beanScanner;

    public void setBeans() throws Exception {
        classSet = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        setBeansInterfaces();

        LinkedList<Class<?>> classQueue = new LinkedList<>(classSet);

        while (!classQueue.isEmpty()) {
            Class<?> clazz = classQueue.pollFirst();
            if (beans.containsKey(clazz.getSimpleName())) continue;

            Constructor<?> constructor = getConstructor(clazz);
            if (constructor == null) continue;

            Object o = getInstance(constructor);
            if (o == null) {
                classQueue.addLast(clazz);
                continue;
            }
            beans.put(clazz.getSimpleName(), o);


            setInterfaces(clazz, o);
        }
        setAutowiredField(classSet, Autowired.class);
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    private void setBeansInterfaces() {
        for (Class<?> clazz : classSet) {
            for (Class<?> anInterface : clazz.getInterfaces()) {
                beanInterfaces.put(anInterface.getSimpleName(), clazz);
            }
        }
    }

    private Object getInstance(Constructor<?> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        LinkedList<Object> classQueue = new LinkedList<>();
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            Object parameter = null;
            if (parameterType.isInterface()) {
                Class<?> clazz = beanInterfaces.get(parameterType.getSimpleName());
                if (clazz == null) return null;
                parameter = beans.get(clazz.getSimpleName());
                if (parameter == null) return null;
            }
            if (!parameterType.isInterface()) {
                parameter = beans.get(parameterType.getSimpleName());
                if (parameter == null) return null;
            }
            classQueue.addLast(parameter);
        }
        if (classQueue.isEmpty()) return constructor.newInstance();
        System.out.println(classQueue.toArray(Object[]::new).toString());
        return constructor.newInstance(classQueue.toArray());
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 0) return null;
        if (constructors.length == 1 && constructors[0].getParameterCount() == 0) return constructors[0];

        Constructor<?> autowiredConstructor = getAutowiredConstructor(constructors);
        if (autowiredConstructor != null) {
            return autowiredConstructor;
        }

        Constructor<?> constructor = null;
        for (Constructor<?> c : constructors) {
            if (constructor == null) {
                constructor = c;
                continue;
            }
            constructor = c.getParameterCount() > constructor.getParameterCount() ? c : constructor;
        }
        return constructor;
    }

    private Constructor<?> getAutowiredConstructor(Constructor<?>[] constructors) {
        return Stream.of(constructors).filter(constructor -> constructor.isAnnotationPresent(Autowired.class)).findFirst().orElse(null);
    }

    private void setInterfaces(Class<?> clazz, Object componentObject) {
        Class<?>[] interfaces = clazz.getInterfaces();

        for (Class<?> anInterface : interfaces) {
            if (beans.containsKey(anInterface.getSimpleName())) continue;
            beans.put(anInterface.getSimpleName(), componentObject);
            System.out.println("[Component] " + anInterface.getSimpleName() + " : " + componentObject);
        }
    }

    private void setAutowiredField(Set<Class<?>> collect, final Class<? extends Annotation> annotation) throws IllegalAccessException {
        for (Class<?> clazz : collect) {
            List<Field> fieldList = stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(annotation)).toList();
            if (fieldList.isEmpty()) continue;
            for (Field field : fieldList) {
                field.setAccessible(true);
                Object fieldObject = beans.get(field.getType().getSimpleName());
                if (fieldObject == null) continue;
                for (Class<?> anInterface : clazz.getInterfaces()) {
                    Object o = beans.get(anInterface.getSimpleName());
                    if (o == null) continue;
                    field.set(o, fieldObject);
                }
                field.setAccessible(false);
            }
        }
    }
}
