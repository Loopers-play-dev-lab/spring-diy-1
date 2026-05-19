package com.diy.framework.web.bean;

import com.diy.framework.web.bean.annotation.Autowired;
import com.diy.framework.web.bean.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BeanContainer {
    private final BeanScanner beanScanner = new BeanScanner();
    private final Map<String, Object> container = new ConcurrentHashMap<>();

    public BeanContainer() {
        initializingContainer();
    }

    /**
     * 우선 component가 붙은 대상을 모두 가져오고
     * 생성자가 하나면 그걸로 생성
     * 생성자가 두 개 이상일 경우 Autowired가 붙은 걸로 생성
     * 그렇게 찾은 Constructor 로 재귀 탐색
     * <p>
     * Constructor를 돌면서 parameter 개수를 확인
     * 만약 parameter 개수가 0이면 그대로 넣고 아니면 해당 파라미터를 다시 생성하는 것으로 진행
     */
    private void initializingContainer() {
        Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        classes.forEach(clazz -> {
            Field[] declaredFields = clazz.getDeclaredFields();
            Arrays.stream(declaredFields).forEach(field -> {
                if (field.getType().equals(clazz)) throw new RuntimeException("circular dependency found");
            });

        });
        classes.forEach(clazz -> createBeanWithClassType(classes, clazz));
    }

    private void createBeanWithClassType(Set<Class<?>> classes, Class<?> clazz) {
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            Constructor<?> targetConstructor = null;
            if (constructors.length == 1) {
                targetConstructor = constructors[0];
            } else {
                for (Constructor<?> constructor : constructors) {
                    if (constructor.isAnnotationPresent(Autowired.class)) {
                        targetConstructor = constructor;
                        break;
                    }
                }
            }

            if (Objects.isNull(targetConstructor)) {
                throw new RuntimeException("Not Appropriated constructor found for: " + clazz);
            }

            if (targetConstructor.getParameterCount() == 0) {
                if (container.containsKey(clazz.getSimpleName())) return;
                container.put(clazz.getSimpleName(), targetConstructor.newInstance());
            } else {
                Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
                int parameterCount = targetConstructor.getParameterCount();
                Object[] parameters = new Object[parameterCount];
                for (int i = 0; i < parameterCount; i++) {
                    final int index = i;
                    Class<?> parameterClass = parameterTypes[i];
                    if (targetConstructor.getParameters()[i].isAnnotationPresent(Autowired.class)) {
                        String name = targetConstructor.getParameters()[i].getAnnotation(Autowired.class).beanValue();
                        findBean(name).ifPresentOrElse(bean -> parameters[index] = bean,
                                () -> {
                                    for (Class ele : classes) {
                                        if (ele.getSimpleName().equals(name)) {
                                            createBeanWithClassType(classes, ele);
                                        }
                                    }

                                    parameters[index] = getBean(name);
                                });
                    } else {
                        findBean(parameterClass)
                                .ifPresentOrElse(bean -> parameters[index] = bean,
                                        () -> {
                                            List<Class<?>> list = classes.stream()
                                                    .filter(parameterClass::isAssignableFrom)
                                                    .toList();

                                            if (list.size() > 1) {
                                                throw new RuntimeException(parameterClass + " constructor parameters are more than two");
                                            } else if (list.isEmpty()) {
                                                throw new RuntimeException(parameterClass + " no matched parameter bean found");
                                            }

                                            Class<?> targetClass = list.getFirst();
                                            createBeanWithClassType(classes, targetClass);
                                            parameters[index] = getBean(targetClass);
                                        });
                    }
                }
                if (container.containsKey(clazz.getSimpleName())) return;
                container.put(clazz.getSimpleName(), targetConstructor.newInstance(parameters));
            }
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return findBean(clazz)
                .orElseThrow(() -> new RuntimeException("no matched Bean Found class: " + clazz ));
    }

    public Object getBean(String name) {
        return findBean(name)
                .orElseThrow(() -> new RuntimeException("no matched Bean Found name: " + name));
    }

    public <T> Optional<T> findBean(Class<T> clazz) {
        List<T> beanList = container.values()
                .stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();

        if (beanList.isEmpty()) {
            return Optional.empty();
        }

        if (beanList.size() == 1) {
            return Optional.of(beanList.getFirst());
        }

        throw new RuntimeException("more than two bean candidates are found");
    }

    public Optional<Object> findBean(String name) {
        Object bean = container.get(name);
        if (Objects.isNull(bean)) {
            return Optional.empty();
        }

        return Optional.of(bean);
    }
}
