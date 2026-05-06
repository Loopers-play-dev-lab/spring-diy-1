package com.diy.framework.web.bean;

import com.diy.framework.web.bean.annotation.Autowired;
import com.diy.framework.web.bean.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
        classes.forEach(this::createBeanWithClassType);
    }

    private void createBeanWithClassType(Class<?> clazz) {
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
                container.put(clazz.getName(), targetConstructor.newInstance());
            } else {
                Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
                int parameterCount = targetConstructor.getParameterCount();
                Object[] parameters = new Object[parameterCount];
                for (int i = 0; i < parameterCount; i++) {
                    final int index = i;
                    Class<?> parameterClass = parameterTypes[i];
                    findBean(parameterClass)
                            .ifPresentOrElse(bean -> parameters[index] = bean,
                                    () -> {
                                        createBeanWithClassType(parameterClass);
                                        parameters[index] = getBean(parameterClass);
                                    });
                }
                container.put(clazz.getName(), targetConstructor.newInstance(parameters));
            }
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return findBean(clazz)
                .orElseThrow(() -> new IllegalArgumentException("no matched Bean Found"));
    }

    public <T> Optional<T> findBean(Class<T> clazz) {
        return container.values()
                .stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst();
    }

    public Object getBean(String name) {
        Object bean = container.get(name);
        if (Objects.isNull(bean)) {
            throw new IllegalArgumentException(name + " bean is not found");
        }

        return bean;
    }
}
