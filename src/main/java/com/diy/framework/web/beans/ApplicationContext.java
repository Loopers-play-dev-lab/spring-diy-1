package com.diy.framework.web.beans;

import com.diy.app.AppConfig;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.beans.factory.annotation.Autowired;
import com.diy.framework.web.beans.factory.annotation.Bean;
import com.diy.framework.web.beans.factory.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

public class ApplicationContext {

    private Set<Class<?>> beanClasses = new HashSet<>();
    private static final Map<String, Object> beanMap = new HashMap<>();
    private BeanScanner beanScanner;

    public ApplicationContext(String... beanPackages) {
        beanScanner = new BeanScanner(beanPackages);
        init();
    }

    private void init() {
        beanClasses.addAll(beanScanner.scanClassesTypeAnnotatedWith(Component.class));
        beanClasses.forEach(clazz -> {
            if (isBeanInitialized(clazz)) {
                return;
            }

            Object bean = createInstance(clazz);
            Arrays.stream(clazz.getMethods()).forEach(method -> {
                boolean hasBean = method.isAnnotationPresent(Bean.class);
                if (hasBean) {
                    String beanName = method.getDeclaredAnnotation(Bean.class).value();
                    if (beanName == null) {
                        beanName = method.getReturnType().getName();
                    }
                    try {
                        Object methodResult = method.invoke(bean);
                        saveBean(beanName, methodResult);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            saveBean(bean.getClass().getName(), bean);
        });
    }

    public Object findBean(String beanName) {
        Object bean = beanMap.get(beanName);
        if (bean == null) {
            throw new RuntimeException("Bean을 찾을 수 없음. " + beanName);
        }
        return bean;
    }

    private Object createInstance(Class<?> clazz) {
        Constructor<?> constructor = findConstructor(clazz);

        try {
            constructor.setAccessible(true);
            Object[] parameters = getConstructorParameters(constructor);

            return constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        if (declaredConstructors.length == 1) {
            return declaredConstructors[0];
        }

        return findAutowiredConstructor(declaredConstructors);
    }

    private Constructor<?> findAutowiredConstructor(Constructor<?>[] constructors) {
        Constructor[] autowiredConstructors = Arrays.stream(constructors).filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if (autowiredConstructors.length != 1) {
            throw new RuntimeException("Autowired 생성자를 찾을 수 없음");
        }

        return autowiredConstructors[0];
    }

    private Object[] getConstructorParameters(Constructor<?> constructor) {
        List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes()).toList();

        if (!beanClasses.containsAll(parameterTypes)) {
            throw new RuntimeException("매개변수가 Bean이 아님");
        }

        return parameterTypes.stream().map(parameterType ->  {
            if (isBeanInitialized(parameterType)) {
                return beanMap.values().stream().findFirst().get();
            }

            Object bean = createInstance(parameterType);
            saveBean(bean.getClass().getName(), bean);

            return bean;
        }).toArray();
    }

    private boolean isBeanInitialized(final Class<?> parameterType) {
        return beanMap.values().stream().anyMatch(bean -> bean.getClass().equals(parameterType));
    }

    private void saveBean(String beanName, Object bean) {
        beanMap.put(beanName, bean);
    }

//    public void register(Set<Class<?>> clazzSet) {
//        for (Class<?> aClass : clazzSet) {
//            register(aClass);
//        }
//    }
//
//    public void register(Class<?> clazz) {
//        Object o = create(clazz, beanMap.values().toArray());
//        beanMap.put(clazz.descriptorString(), o);
//    }
//
//    public void inject(Set<Constructor> constructors) {
//        for (Constructor constructor : constructors) {
//            inject(constructor);
//        }
//    }
//
//    public void inject(Constructor constructor) {
//        constructor.setAccessible(true);
//        Parameter[] parameters = constructor.getParameters();
//        for (Parameter parameter : parameters) {
//            Object o = beanMap.get(parameter.getType());
//        }
//        Class declaringClass = constructor.getDeclaringClass();
//    }
//
//    private static Object create(Class<?> clazz, Object... args) {
//        try {
//            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
//            Optional<Object> o = Optional.empty();
//            for (Constructor<?> declaredConstructor : declaredConstructors) {
//                try {
//                    boolean isAllBeanStored = new HashSet<>(Arrays.stream(args)
//                            .map(Object::getClass)
//                            .toList())
//                            .containsAll(
//                                    Arrays.stream(declaredConstructor.getParameters()).map(Parameter::getType).toList()
//                            );
//                    if (!isAllBeanStored) { throw new IllegalArgumentException(); }
//                    o = Optional.of(declaredConstructor.newInstance(args));
//                    System.out.println("Bean 생성 => " + o);
//                    break;
//                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
//                    throw new RuntimeException(e);
//                }
//                catch (IllegalArgumentException ignored) {}
//            }
//            return o.orElseThrow();
//        } catch (Exception e) {
//            throw new RuntimeException("Bean 객체를 생성할 수 없습니다.");
//        }
//    }

}
