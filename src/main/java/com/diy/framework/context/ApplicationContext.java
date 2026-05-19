package com.diy.framework.context;

import com.diy.framework.beans.definition.BeanDefinition;
import com.diy.framework.beans.definition.ConstructureBeanDefinition;
import com.diy.framework.beans.definition.MethodBeanDefinition;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    private final String basePackage;
    private final Set<BeanDefinition> beanDefinitionRegistry = new HashSet<>();
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(String basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        BeanScanner beanScanner = new BeanScanner("com.diy.framework", basePackage);
        Set<Class<?>> beanClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        beanClasses.forEach(this::registerBeanDefinition);
        beanDefinitionRegistry.forEach(this::registerBean);
    }

    public Set<String> getBeanNames() {
        return Collections.unmodifiableSet(this.beans.keySet());
    }

    public Object getBean(String beanName) {
        Object bean = beans.get(beanName);
        if (bean == null) {
            throw new RuntimeException("Bean not found");
        }

        return bean;
    }

    private void registerBeanDefinition(Class<?> clazz) {
        BeanDefinition constructureBeanDefinition = new ConstructureBeanDefinition(clazz);
        beanDefinitionRegistry.add(constructureBeanDefinition);

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> {
                    BeanDefinition methodBeanDefinition = new MethodBeanDefinition(method, constructureBeanDefinition.getBeanName());
                    beanDefinitionRegistry.add(methodBeanDefinition);
                });
    }

    private void registerBean(BeanDefinition beanDefinition) {
        String beanName = beanDefinition.getBeanName();
        if (beans.containsKey(beanName)) {
            return;
        }

        createInstance(beanDefinition);
    }

    private Object createInstance(BeanDefinition beanDefinition) {
        Executable factoryMethod = beanDefinition.getFactoryMethod();

        try {
            factoryMethod.setAccessible(true);
            Class<?>[] parameterTypes = beanDefinition.getParameterTypes();
            Object[] arguments = resolveArguments(parameterTypes);

            if (beanDefinition instanceof ConstructureBeanDefinition constructureBeanDefinition) {
                Constructor<?> constructor = constructureBeanDefinition.getFactoryMethod();
                Object bean = constructor.newInstance(arguments);
                saveBean(beanDefinition.getBeanName(), bean);
                return bean;
            }

            if (beanDefinition instanceof MethodBeanDefinition methodBeanDefinition) {
                Object factoryBean = resolveFactoryBean(methodBeanDefinition);
                Method method = methodBeanDefinition.getFactoryMethod();
                Object bean = method.invoke(factoryBean, arguments);
                saveBean(beanDefinition.getBeanName(), bean);
                return bean;
            }

            throw new RuntimeException("Not support factory method");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            factoryMethod.setAccessible(false);
        }
    }

    private Object[] resolveArguments(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
                .map(parameterType -> {
                    BeanDefinition beanDefinition = findBeanDefinition(parameterType);
                    return resolveBean(beanDefinition);
                })
                .toArray();
    }

    private BeanDefinition findBeanDefinition(Class<?> type) {
        List<BeanDefinition> definitions = beanDefinitionRegistry.stream()
                .filter(definition -> {
                    if(type.isInterface()) {
                        return type.isAssignableFrom(definition.getBeanClass());
                    }

                    return definition.getBeanClass().equals(type);
                })
                .toList();

        if (definitions.isEmpty()) {
            throw new RuntimeException("BeanDefinition not found");
        }
        if (definitions.size() > 1) {
            throw new RuntimeException("Multiple BeanDefinition found");
        }

        return definitions.getFirst();
    }

    private void saveBean(String beanName, Object bean) {
        beans.put(beanName, bean);
    }

    private Object resolveBean(BeanDefinition beanDefinition) {
        if (!beans.containsKey(beanDefinition.getBeanName())) {
            createInstance(beanDefinition);
        }

        return beans.get(beanDefinition.getBeanName());
    }

    private Object resolveFactoryBean(BeanDefinition beanDefinition) {
        String factoryBeanName = beanDefinition.getFactoryBeanName();

        if (!beans.containsKey(factoryBeanName)) {
            BeanDefinition factoryBeanDefinition = findBeanDefinition(factoryBeanName);
            return createInstance(factoryBeanDefinition);
        }

        return beans.get(factoryBeanName);
    }

    private BeanDefinition findBeanDefinition(String beanName) {
        return beanDefinitionRegistry.stream()
                .filter(definition -> definition.getBeanName().equals(beanName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("BeanDefinition not found"));
    }
}
