package com.diy.framework.web.context;

import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.context.annotation.Autowired;
import com.diy.framework.web.context.annotation.Bean;
import com.diy.framework.web.context.annotation.Component;
import com.diy.framework.web.context.annotation.Configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanContext {
    private final BeanScanner beanScanner;
    private final Map<String, Object> beanMap;
    private final Set<String> beanNames;

    public BeanContext(String packageName) {
        beanScanner = new BeanScanner(packageName);
        beanMap = new HashMap<String, Object>();
        beanNames = new HashSet<String>();
        init();
    }

    private void init() {
        Set<Class<?>> configBean = beanScanner.scanClassesTypeAnnotatedWith(Configuration.class);
        Set<Class<?>> componentBean = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> clazz : configBean) {
            if(clazz.isAnnotationPresent(Configuration.class)){
                createBean(clazz);
            }
        }

        for(Class<?> clazz : componentBean){
            if(clazz.isAnnotationPresent(Configuration.class)){
                continue;
            }

            if(clazz.isAnnotationPresent(Component.class)){
                createBean(clazz);
            }
        }
    }

    private Object createBean(Class<?> clazz) {
        String beanName = toBeanName(clazz);
        if(beanNames.contains(beanName)){
            return beanMap.get(beanName);
        }

        Constructor<?> constructor = findConstructor(clazz);
        try {
            constructor.setAccessible(true);
            Object[] params = getConstructArguments(constructor);
            Object bean = constructor.newInstance(params);
            beanMap.put(beanName, bean);
            beanNames.add(beanName);

            createMethodBean(clazz, bean);

            return beanMap.get(beanName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private void createMethodBean(Class<?> clazz, Object bean) {
        for(Method method : clazz.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Bean.class)) {
                Bean beanValue = method.getAnnotation(Bean.class);
                String beanName = beanValue.value();

                if(beanNames.contains(beanName)){
                    continue;
                }

                try {

                    Object[] methodParams = resolveMethodArguments(method);
                    Object newBean = method.invoke(bean, methodParams);
                    beanMap.put(beanName, newBean);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Object[] resolveMethodArguments(Method method) {
        Object[] params = new Object[method.getParameterCount()];
        Class<?>[] fields = method.getParameterTypes();

        for(int i = 0; i < params.length; i++){
            params[i] = createBean(fields[i]);
        }

        return params;
    }

    private Object[] getConstructArguments(Constructor<?> constructor) {
        Object[] params = new Object[constructor.getParameterCount()];
        List<Class<?>> fields = Arrays.stream(constructor.getParameterTypes()).toList();

        for(int i = 0; i < params.length; i++){
            params[i] = createBean(fields.get(i));
        }

        return params;
    }

    private Constructor<?> findConstructor(Class<?> clazz){
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if(constructors.length == 1){
            return constructors[0];
        }

        return findAutowireConstructor(constructors);
    }

    private Constructor<?> findAutowireConstructor(Constructor<?>[] constructors){
        Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if(autowiredConstructors.length != 1){
            throw new RuntimeException("Autowired constructor not found");
        }

        return autowiredConstructors[0];
    }

    public static String toBeanName(Class<?> clazz) {
        String name = clazz.getSimpleName();
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
}
