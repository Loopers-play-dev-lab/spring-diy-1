package com.diy.framework.context;

import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.context.annotation.*;
import com.diy.framework.web.mvc.controller.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ApplicationContext {

    private final String basePackage;
    private final Set<Class<?>> beanClasses = new HashSet<>();
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(String basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        BeanScanner beanScanner = new BeanScanner(this.basePackage);
        beanClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Component.class));

        for(Class<?> clazz : beanClasses) {
            if(isBeanInitialized(clazz)) {
                continue;
            }

            Object bean = createInstance(clazz);
            saveBean(resolveBeanName(clazz), bean);

            registerExternalBeans(clazz, bean);
        }
    }

    private Object createInstance(Class<?> clazz) {
        Constructor<?> constructor = findConstructor(clazz);

        try{
            constructor.setAccessible(true);

            final Object[] parameters = getConstructorParameters(constructor);

            return constructor.newInstance(parameters);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }   finally {
            constructor.setAccessible(false);
        }
    }

    private Object[] getConstructorParameters(Constructor<?> constructor) {
        List<Class<?>> parameterTypes =  Arrays.stream(constructor.getParameterTypes()).toList();

        if(!beanClasses.containsAll(parameterTypes)) {
            throw new RuntimeException("파라미터 타입이 bean이 아닙니다.");
        }

        return parameterTypes.stream().map(parameterType -> {
            String beanName = resolveBeanName(parameterType);

            if(isBeanInitialized(parameterType)) {
                return beans.get(beanName);
            }

            Object bean = createInstance(parameterType);
            saveBean(beanName, bean);

            return bean;
        }).toArray();
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if(constructors.length == 1) {
            return constructors[0];
        }

        return findAutowiredConstructor(constructors);
    }

    private Constructor<?> findAutowiredConstructor(Constructor<?>[] constructors) {
        Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if(autowiredConstructors.length == 0) {
            throw new RuntimeException("Autowired 생성자가 없습니다.");
        }

        if(autowiredConstructors.length > 1) {
            throw new RuntimeException("Autowired 생성자는 하나여야합니다.");
        }

        return autowiredConstructors[0];
    }

    private void saveBean(String name, Object bean) {
        if(beans.containsKey(name)) {
            throw new RuntimeException("동일한 이름의 빈이 이미 존재합니다.");
        }

        beans.put(name, bean);
    }

    private String resolveBeanName(Class<?> clazz) {
        String clazzName = clazz.getSimpleName();
        return clazzName.substring(0, 1).toLowerCase() + clazzName.substring(1);
    }

    private boolean isBeanInitialized(Class<?> clazz) {
        return beans.containsKey(resolveBeanName(clazz));
    }

    private void registerExternalBeans(Class<?> clazz, Object bean) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Bean.class))
                .forEach(method -> {
                    try {
                        method.setAccessible(true);
                        saveBean(method.getDeclaredAnnotation(Bean.class).name(), method.invoke(bean));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } finally {
                        method.setAccessible(false);
                    }
                });
    }

    public Object getBean(Class<?> clazz) {
        return beans.get(resolveBeanName(clazz));
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    public Map<String, Controller> getControllersMapping() {
        Map<String, Controller> controllersMapping = new HashMap<>();

        beans.values().stream()
                .filter(bean -> bean.getClass().isAnnotationPresent(RequestMapping.class)).toList()
                .forEach(bean -> controllersMapping.put(bean.getClass().getDeclaredAnnotation(RequestMapping.class).path(), (Controller) bean));

        return controllersMapping;
    }
}
