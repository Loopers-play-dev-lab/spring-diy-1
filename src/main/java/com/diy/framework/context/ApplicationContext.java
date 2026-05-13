package com.diy.framework.context;

import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    private final String[] basePackages;

    private final Set<Class<?>> componentClasses = new HashSet<>();
    private final Map<String, Object> beans = new HashMap<>();
    private final Map<Class<?>, List<String>> beanNamesByType = new HashMap<>();
    private final Map<String, Controller> handlerMap = new HashMap<>();

    public ApplicationContext(String... basePackages) {
        this.basePackages = basePackages;
    }

    public void initialize() {
        BeanScanner beanScanner = new BeanScanner(basePackages);
        componentClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Component.class));
        componentClasses.forEach(this::registerConstructorBean);
        componentClasses.forEach(this::registerMethodBean);
        componentClasses.forEach(this::registerHandlerMapping);
    }

    public Object getBean(String name) {
        Object bean = beans.get(name);
        if (bean == null) {
            throw new RuntimeException("Bean not found");
        }

        return bean;
    }

    public Map<String, Controller> getHandlerMap() {
        return Collections.unmodifiableMap(handlerMap);
    }

    private void registerConstructorBean(Class<?> clazz) {
        String beanName = getBeanName(clazz);
        if (beans.containsKey(beanName)) {
            return;
        }

        if (clazz.isInterface()) {
            Class<?> implementClass = resolveImplementation(clazz);
            registerConstructorBean(implementClass);
            return;
        }

        if (!componentClasses.contains(clazz)) {
            throw new RuntimeException("Not componentClass");
        }

        try {
            Constructor<?> constructor = resolveConstructor(clazz);
            Object[] args = resolveArgs(constructor);
            Object instance = constructor.newInstance(args);
            putBean(beanName, clazz, instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> resolveConstructor(Class<?> clazz) {
        List<Constructor<?>> constructors = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .toList();

        if (constructors.isEmpty()) {
            return Arrays.stream(clazz.getDeclaredConstructors())
                    .filter(c -> c.getParameterCount() == 0)
                    .findFirst()
                    .orElse(clazz.getDeclaredConstructors()[0]);
        }
        if (constructors.size() > 1) {
            throw new RuntimeException("Multiple @Autowired constructors");
        }

        return constructors.getFirst();
    }

    private Class<?> resolveImplementation(Class<?> interfaceType) {
        List<Class<?>> implementations = componentClasses.stream()
                .filter(c -> !c.isInterface() && interfaceType.isAssignableFrom(c))
                .toList();

        if (implementations.isEmpty()) {
            throw new RuntimeException("Bean not found");
        }
        if (implementations.size() > 1) {
            throw new RuntimeException("Multiple implementations found");
        }

        return implementations.getFirst();
    }

    private void registerMethodBean(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> {
                    String beanName = getBeanName(method);
                    if (beans.containsKey(beanName)) {
                        return;
                    }
                    try {
                        Object factory = getBean(clazz);
                        Object instance = method.invoke(factory, resolveArgs(method));
                        putBean(beanName, method.getReturnType(), instance);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void registerHandlerMapping(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        Object bean = beans.get(getBeanName(clazz));
        if (bean instanceof Controller controller) {
            String url = clazz.getAnnotation(RequestMapping.class).value();
            handlerMap.put(url, controller);
        }
    }

    private Object[] resolveArgs(Executable executable) {
        return Arrays.stream(executable.getParameterTypes())
                .map(type -> {
                    registerConstructorBean(type);
                    return getBean(type);
                })
                .toArray();
    }

    private void putBean(String beanName, Class<?> clazz, Object instance) {
        beans.put(beanName, instance);
        beanNamesByType.computeIfAbsent(clazz, c -> new ArrayList<>()).add(beanName);
        Arrays.stream(clazz.getInterfaces())
                .forEach(i -> beanNamesByType.computeIfAbsent(i, k -> new ArrayList<>()).add(beanName));
    }

    private String getBeanName(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    private String getBeanName(Method method) {
        String value = method.getAnnotation(Bean.class).value();
        return value.isEmpty() ? method.getName() : value;
    }

    private <T> T getBean(Class<T> type) {
        List<String> names = beanNamesByType.get(type);

        if (names == null || names.isEmpty()) {
            throw new RuntimeException("Bean not found");
        }
        if (names.size() > 1) {
            throw new RuntimeException("Multiple beans of same type");
        }

        return type.cast(beans.get(names.getFirst()));
    }
}
