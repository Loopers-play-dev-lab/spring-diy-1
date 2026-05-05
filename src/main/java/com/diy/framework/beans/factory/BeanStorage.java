package com.diy.framework.beans.factory;

import com.diy.framework.beans.annotations.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanStorage {

    private static BeanStorage instance;

    private final List<?> beans;
    private final BeanScanner bc = new BeanScanner("com.diy.app");

    private BeanStorage() {
        Set<Class<?>> classes = bc.scanClassesTypeAnnotatedWith(Component.class);
        beans = getBeans(classes);
        setFields(getFields());
    }

    public static BeanStorage getInstance() {
        if (Objects.isNull(instance)) instance = new BeanStorage();
        return instance;
    }

    private static List<?> getBeans(Set<Class<?>> classes) {
        return classes.stream().map(clazz -> {
            Constructor<?> constructor = null;
            try {
                constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            } finally {
                if (constructor != null) {
                    constructor.setAccessible(false);
                }
            }
        }).toList();
    }

    public <T> List<T> getBeans(final Class<T> classType) {
        return beans.stream()
                .filter(classType::isInstance)
                .map(classType::cast)
                .toList();
    }

    public Map<Class<?>, Set<Field>> getFields() {
        Map<Class<?>, Set<Field>> classToField = new HashMap<>();
        for (Class<?> clazz : beans.stream().map(Object::getClass).toList()) {
            classToField.put(clazz, bc.scanField(clazz));
        }
        return classToField;
    }

    public void setFields(Map<Class<?>, Set<Field>> classToFields) {
        for (Object bean : beans) {
            for (Field field : classToFields.get(bean.getClass())) {
                Object target = null;
                for (Object targetBean : beans) {
                    if (!field.getType().isInstance(targetBean)) continue;
                    if (Objects.nonNull(target)) throw new IllegalArgumentException("2개 이상의 빈이 존재");
                    target = targetBean;
                }
                if (Objects.isNull(target)) throw new IllegalArgumentException("존재하지 않는 빈" + field.getName());
                try {
                    field.setAccessible(true);
                    field.set(bean, target);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    // 그냥 자기 필드에 있는 애들 쭉 순회하면서 자기 자신으로 돌아오는지 확인
    // 그러면서 해당 인스턴스가 아닌 애들이 존재하면 탈락
    // 순환 참조
//    public boolean checkValidation(Map<Class<?>, Set<Field>> classMap) {
//        Set<Class<?>>
//    }
}
