package com.diy.framework.context;

import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.context.annotation.*;
import com.diy.framework.web.utils.ControllerV2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanContext {
    private final BeanScanner beanScanner;
    private final Map<String, Object> beanMap;
    private final Set<String> beanNames;
    private final Map<String, ControllerV2> controllerMap;

    public BeanContext(String packageName) {
        beanScanner = new BeanScanner(packageName);
        beanMap = new HashMap<String, Object>();
        beanNames = new HashSet<String>();
        controllerMap = new HashMap<>();
        init();
    }

    public void init() {
        Set<Class<?>> componentBean = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        Set<Class<?>> controllerBean = beanScanner.scanClassesTypeAnnotatedWith(Controller.class);

        for(Class<?> clazz : componentBean){
            if(clazz.isAnnotationPresent(Component.class)){
                createBean(clazz);
            }
        }

        for(Class<?> clazz : controllerBean){
            if(clazz.isAnnotationPresent(Controller.class)){
                initController(clazz);
            }
        }
    }

    public Map<String, ControllerV2> getControllerMap() {
        return controllerMap;
    }

    private void initController(Class<?> clazz) {
        System.out.print(clazz.getName() + " ");
        System.out.println(clazz.isAnnotationPresent(Controller.class));
        if(!clazz.isAnnotationPresent(Controller.class)) {
            return;
        }

        String path = clazz.getAnnotation(Controller.class).value();
        if(controllerMap.containsKey(path)){
            return;
        }

        ControllerV2 controllerBean = (ControllerV2) createBean(clazz);
        controllerMap.put(path, controllerBean);
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

    private static String toBeanName(Class<?> clazz) {
        String name = clazz.getSimpleName();
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
}
