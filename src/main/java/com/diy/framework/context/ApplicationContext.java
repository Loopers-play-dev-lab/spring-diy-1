package com.diy.framework.context;

import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.context.annotation.RequestMapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Collection;
import java.util.Collections;

public class ApplicationContext {

    private final String basePackage;
    private final Set<Class<?>> beanClasses = new HashSet<>();
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(final String basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final BeanScanner beanScanner = new BeanScanner(basePackage);
        beanClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Component.class));
        beanClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Controller.class));

        beanClasses.forEach(clazz -> {
            if (isBeanInitialized(clazz)) {
                return;
            }

            final Object bean = createInstance(clazz);
            saveBean(generateBeanName(clazz), bean);
        });

        registerBeanMethods();
    }

    private void registerBeanMethods() {
        // 이미 등록된 @Component 빈들을 순회
        new ArrayList<>(beans.values()).forEach(componentBean -> {
            final Method[] methods = componentBean.getClass().getDeclaredMethods();
            Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(Bean.class))
                    .forEach(method -> registerBeanFromMethod(componentBean, method));
        });
    }

    private void registerBeanFromMethod(final Object componentBean, final Method method) {
        try {
            final Bean beanAnnotation = method.getAnnotation(Bean.class);

            // 이름: @Bean("name")이면 name, 없으면 메서드명
            final String beanName = beanAnnotation.value().isEmpty()
                    ? method.getName()
                    : beanAnnotation.value();

            method.setAccessible(true);
            final Object bean = method.invoke(componentBean);
            method.setAccessible(false);

            saveBean(beanName, bean);
        } catch (Exception e) {
            throw new RuntimeException("@Bean 메서드 실행 실패", e);
        }
    }

    private Object createInstance(final Class<?> clazz) {
        final Constructor<?> constructor = findConstructor(clazz);

        try {
            constructor.setAccessible(true);
            final Object[] parameters = getConstructorParameters(constructor);

            return constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private Constructor<?> findConstructor(final Class<?> clazz) {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return findAutowiredConstructor(constructors);
    }

    private Constructor<?> findAutowiredConstructor(final Constructor<?>[] constructors) {
        final Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if (autowiredConstructors.length != 1) {
            throw new RuntimeException("Autowired constructor not found");
        }

        return autowiredConstructors[0];
    }

    private Object[] getConstructorParameters(final Constructor<?> constructor) {
        final List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes()).toList();

        if (!beanClasses.containsAll(parameterTypes)) {
            throw new RuntimeException("parameter is not bean");
        }

        return parameterTypes.stream().map(parameterType -> {
            if (isBeanInitialized(parameterType)) {
                return beans.values().stream()
                        .filter(bean -> bean.getClass().equals(parameterType))
                        .findFirst().get();
            }

            final Object bean = createInstance(parameterType);
            saveBean(generateBeanName(parameterType), bean);

            return bean;
        }).toArray();
    }

    public Collection<Object> getBeans() {
        return Collections.unmodifiableCollection(beans.values());
    }

    public Map<String, com.diy.framework.web.mvc.Controller> getControllerMapping() {
        final Map<String, com.diy.framework.web.mvc.Controller> mapping = new HashMap<>();

        beans.values().stream()
                .filter(bean -> bean instanceof com.diy.framework.web.mvc.Controller)
                .forEach(bean -> {
                    final RequestMapping annotation =
                            bean.getClass().getAnnotation(RequestMapping.class);
                    if (annotation != null) {
                        mapping.put(annotation.value(), (com.diy.framework.web.mvc.Controller) bean);
                    }
                });

        return mapping;
    }

    private boolean isBeanInitialized(final Class<?> clazz) {
        return beans.values().stream().anyMatch(bean -> bean.getClass().equals(clazz));
    }

    private void saveBean(final String beanName, final Object bean) {
        beans.put(beanName, bean);
    }

    private String generateBeanName(final Class<?> clazz) {
        final String simpleName = clazz.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
