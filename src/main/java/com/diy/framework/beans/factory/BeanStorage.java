package com.diy.framework.beans.factory;

import com.diy.framework.beans.annotations.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanStorage {

    private static BeanStorage instance;

    private final List<Object> beans;
    private final BeanScanner bc = new BeanScanner("com.diy.app");

    private BeanStorage() {
        Set<Class<?>> classes = bc.scanClassesTypeAnnotatedWith(Component.class);
        Map<Class<?>, Set<Field>> classToFields = getFields(classes);
        List<Class<?>> sortedClasses = topologicalSort(classToFields);
        beans = makeBeans(sortedClasses, classToFields);
    }

    public static BeanStorage getInstance() {
        if (Objects.isNull(instance)) instance = new BeanStorage();
        return instance;
    }

    private static List<Object> makeBeans(List<Class<?>> classes, Map<Class<?>, Set<Field>> classToFields) {
        List<Object> results = new ArrayList<>();

        for (Class<?> clazz : classes) {
            Constructor<?> constructor = null;
            try {
                constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object bean = constructor.newInstance();
                setFields(bean, results, classToFields);
                results.add(bean);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            } finally {
                if (constructor != null) {
                    constructor.setAccessible(false);
                }
            }
        }

        return results;
    }

    public <T> List<T> getBeans(final Class<T> classType) {
        return beans.stream()
                .filter(classType::isInstance)
                .map(classType::cast)
                .toList();
    }

    public Map<Class<?>, Set<Field>> getFields(Set<Class<?>> classes) {
        Map<Class<?>, Set<Field>> classToField = new HashMap<>();
        for (Class<?> clazz : classes) {
            classToField.put(clazz, bc.scanField(clazz));
        }
        return classToField;
    }

    public static void setFields(Object target, List<Object> nowBeanList, Map<Class<?>, Set<Field>> classToFields) {
        for (Field field : classToFields.get(target.getClass())) {
            Object fieldTarget = null;
            for (Object targetBean : nowBeanList) {
                if (!field.getType().isInstance(targetBean)) continue;
                if (Objects.nonNull(fieldTarget)) throw new IllegalArgumentException("2개 이상의 빈이 존재 : " + field.getType().getName());
                fieldTarget = targetBean;
            }
            if (Objects.isNull(fieldTarget)) throw new IllegalArgumentException("존재하지 않는 빈 : " + field.getType().getName());
            try {
                field.setAccessible(true);
                field.set(target, fieldTarget);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                field.setAccessible(false);
            }
        }
    }

    public List<Class<?>> topologicalSort(Map<Class<?>, Set<Field>> classMap) {
        Map<Class<?>, Set<Class<?>>> map = copyMap(classMap);

        List<Class<?>> result = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();

        while (visited.size() < map.size()) {
            List<Class<?>> canMake = new ArrayList<>();
            for (Map.Entry<Class<?>, Set<Class<?>>> entry : map.entrySet()) {
                if (visited.contains(entry.getKey())) continue;
                if (visited.containsAll(entry.getValue())) {
                    canMake.add(entry.getKey());
                }
            }
            if (canMake.isEmpty()) {
                throw new IllegalArgumentException("순환 참조 혹은 존재하지 않는 빈을 필드로하는 것이 존재합니다");
            }
            visited.addAll(canMake);
            result.addAll(canMake);
        }

        return result;
    }

    private static Map<Class<?>, Set<Class<?>>> copyMap(Map<Class<?>, Set<Field>> classMap) {
        Map<Class<?>, Set<Class<?>>> map = new HashMap<>();
        for (Map.Entry<Class<?>, Set<Field>> entry : classMap.entrySet()) {
            Set<Class<?>> fieldTypes = new HashSet<>();
            for (Field field : entry.getValue()) {
                fieldTypes.add(field.getType());
            }
            map.put(entry.getKey(), fieldTypes);
        }
        return map;
    }
}
