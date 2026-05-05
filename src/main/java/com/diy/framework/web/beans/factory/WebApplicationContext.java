package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.annotations.Autowired;
import com.diy.framework.web.beans.annotations.Component;

import javax.servlet.ServletContextListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Arrays.stream;

public class WebApplicationContext implements ServletContextListener {
    private static volatile WebApplicationContext instance;

    private WebApplicationContext() {
    }

    public static WebApplicationContext getInstance() {
        if (instance == null) {
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            try {
                if (instance == null) {
                    instance = new WebApplicationContext();
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

    private BeanScanner beanScanner;

    public void setBeans() throws Exception {
        Set<Class<?>> classSet = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        setNoRequiredArgsConstructorClasses(classSet);
        setRequiredArgsConstructorClasses(classSet);
        setAutowiredField(classSet, Autowired.class);
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    private void setNoRequiredArgsConstructorClasses(Set<Class<?>> collect) {
        for (Class<?> clazz : collect) {
            if (clazz.getDeclaredConstructors()[0].getParameterCount() > 0) continue;

            Object o = null;
            try {
                o = clazz.getDeclaredConstructors()[0].newInstance();
                setInterfaces(clazz, o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setRequiredArgsConstructorClasses(Set<Class<?>> collect) throws Exception {
        LinkedList<Class<?>> collectList = new LinkedList<>(collect);
        Map<String, Constructor<?>> constructorMap = new HashMap<>();

        for (Class<?> clazz : collectList) {
            if (beans.containsKey(clazz.getSimpleName())) continue;
            if (clazz.getInterfaces().length > 0 && beans.containsKey(clazz.getInterfaces()[0].getSimpleName())) continue;

            Constructor<?> requiredArgsConstructor = null;
            if (constructorMap.containsKey(clazz.getSimpleName())) {
                requiredArgsConstructor = constructorMap.get(clazz.getSimpleName());
            }
            if (!constructorMap.containsKey(clazz.getSimpleName())) {
                requiredArgsConstructor = getRequiredArgsConstructor(clazz);
                if (requiredArgsConstructor != null) constructorMap.put(clazz.getSimpleName(), requiredArgsConstructor);
            }
            if (requiredArgsConstructor == null) continue;

            Object o = createArgsConstructorInstance(requiredArgsConstructor);
            if (o == null) {
                collectList.addLast(clazz);
            }
            setInterfaces(clazz, o);
        }
    }

    private Constructor<?> getRequiredArgsConstructor(Class<?> clazz) {
        List<Class<?>> privateFieldClasses = new LinkedList<>();

        for (Field field : clazz.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isPrivate(modifiers) && Modifier.isFinal(modifiers)) {
                System.out.println(field.getName() + " : " + field.getType().getSimpleName());
                privateFieldClasses.add(field.getType());
            }
        }

        if (privateFieldClasses.isEmpty()) return null;

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            int parameterCount = constructor.getParameterCount();
            if (parameterCount != privateFieldClasses.size()) continue;

            List<Class<?>> parameterObjectList = new LinkedList<>();
            for (Class<?> parameterType : constructor.getParameterTypes()) {
                parameterObjectList.add(parameterType);
            }

            if (parameterObjectList.containsAll(privateFieldClasses) && privateFieldClasses.size() == parameterObjectList.size()) {
                return constructor;
            }
        }

        return null;
    }

    private Object createArgsConstructorInstance(Constructor<?> argsConstructor) throws Exception {
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

    private void setInterfaces(Class<?> clazz, Object componentObject) {
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            beans.put(clazz.getSimpleName(), componentObject);
            System.out.println("[Component] " + clazz.getSimpleName() + " : " + componentObject);
            return;
        }

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
