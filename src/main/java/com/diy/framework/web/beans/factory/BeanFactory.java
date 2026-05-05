package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.annotation.Autowired;
import com.diy.framework.web.beans.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private BeanScanner beanScanner;
    private Map<String, Object> beanMap;

    public BeanFactory(String packageName) {
        beanScanner = new BeanScanner(packageName);
        beanMap = new HashMap<String, Object>();
        startBean();
    }

    private void startBean() {
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

        Constructor<?> constructor = getConstructor(clazz);
        try {
            Object bean = injectBean(constructor, clazz);
            beanMap.put(clazz.getTypeName(), bean);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        return beanMap.get(clazz.getTypeName());
    }

    private Object injectBean(Constructor<?> constructor, Class<?> bean) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object[] params = new Object[constructor.getParameterCount()];
        Field[] fields = bean.getDeclaredFields();

        for(int i = 0; i < params.length; i++){
            params[i] = createBean(fields[i].getType());
        }

        return constructor.newInstance(params);
    }

    private Constructor<?> getConstructor(Class<?> clazz){
        System.out.println(clazz.getTypeName() + " : 생성자");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for(Constructor<?> constructor : constructors){
            System.out.println(constructor.getName() + " : " + constructor.getParameterCount());
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
