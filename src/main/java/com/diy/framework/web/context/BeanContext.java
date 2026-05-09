package com.diy.framework.web.context;

import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.context.annotation.Autowired;
import com.diy.framework.web.context.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanContext {
    private BeanScanner beanScanner;
    private Map<String, Object> beanMap;

    public BeanContext(String packageName) {
        beanScanner = new BeanScanner(packageName);
        beanMap = new HashMap<String, Object>();
        init();
    }

    private void init() {
        Set<Class<?>> bean = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        for(Class<?> clazz : bean){
            if(clazz.isAnnotationPresent(Component.class)){
                createBean(clazz);
            }
        }
    }

    private Object createBean(Class<?> clazz) {
        if(beanMap.containsKey(clazz.getTypeName())){
            return beanMap.get(clazz.getTypeName());
        }

        Constructor<?> constructor = getBeanConstructor(clazz);
        Object[] params = resolveConstructorArguments(constructor, clazz);
        try {
            Object bean = constructor.newInstance(params);
            beanMap.put(clazz.getTypeName(), bean);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        return beanMap.get(clazz.getTypeName());
    }

    private Object[] resolveConstructorArguments(Constructor<?> constructor, Class<?> bean) {
        Object[] params = new Object[constructor.getParameterCount()];
        Field[] fields = bean.getDeclaredFields();

        for(int i = 0; i < params.length; i++){
            params[i] = createBean(fields[i].getType());
        }

        return params;
    }

    private Constructor<?> getBeanConstructor(Class<?> clazz){
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for(Constructor<?> constructor : constructors){
            if(constructor.isAnnotationPresent(Autowired.class)){
                return constructor;
            }
        }

        return constructors[0];
    }

    public Object getBean(String beanName){
        return beanMap.get(beanName);
    }

}
