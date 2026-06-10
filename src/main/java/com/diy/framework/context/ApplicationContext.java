package com.diy.framework.context;

import com.diy.framework.beans.factory.AnnotatedGenericBeanDefinition;
import com.diy.framework.beans.factory.BeanDefinition;
import com.diy.framework.beans.factory.BeanFactory;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.beans.factory.ConfigurationClassBeanDefinition;
import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;
import com.diy.framework.context.support.ApplicationObjectSupport;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.server.WebServer;
import com.diy.framework.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApplicationContext implements BeanFactory {

    public static String APPLICATION_CONTEXT_ATTRIBUTE = ApplicationContext.class.getName();

    private final String basePackage;
    private final List<String> beanDefinitionNames = new ArrayList<>(256);
    private final List<BeanDefinition> beanDefinitionRegistry = new ArrayList<>();
    private final Map<String, Object> beans = new HashMap<>();
    private final Map<Class<?>, Set<String>> allBeanNamesByType = new LinkedHashMap<>();

    public ApplicationContext(final String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public Object getBean(final String name) {
        return this.beans.get(name);
    }

    @Override
    public <T> T getBean(final Class<T> requiredType) {
        final Set<String> beanNames = this.allBeanNamesByType.get(requiredType);

        if (beanNames == null) {
            throw new RuntimeException("Bean not found '" + requiredType + "'");
        } else if (beanNames.size() != 1) {
            throw new RuntimeException("No qualifying bean of type '" + requiredType
                    + "' available: expected single matching bean but found " + beanNames.size() + ": " + String.join(", ", beanNames));
        }

        final String beanName = beanNames.stream().findFirst().get();

        return (T) this.beans.get(beanName);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(final Class<T> type) {
        final String[] beanNames = getBeanNamesForType(type);
        final LinkedHashMap<String, T> result = new LinkedHashMap<>(beanNames.length);

        Arrays.stream(beanNames)
                .forEach(beanName -> result.put(beanName, (T) getBean(beanName)));

        return result;
    }

    @Override
    public <T> String[] getBeanNamesForType(final Class<T> type) {
        final Set<String> beanNames = this.allBeanNamesByType.get(type);

        if (beanNames == null) {
            throw new RuntimeException("Bean names not found '" + type.getName() + "'");
        }

        return beanNames.toArray(String[]::new);
    }

    @Override
    public String[] getBeanNamesForAnnotation(final Class<? extends Annotation> annotationType) {
        List<String> result = new ArrayList<>();

        for (String beanName : this.beanDefinitionNames) {
            final Object bean = getBean(beanName);
            if (bean != null && findAnnotationOnBean(bean, annotationType) != null) {
                result.add(beanName);
            }
        }

        return result.toArray(new String[0]);
    }

    @Override
    public <A extends Annotation> A findAnnotationOnBean(final Object bean, final Class<A> annotationType) {
        final Set<Class<?>> classes = mapToSuperTypes(bean.getClass());
        for (Class<?> clazz : classes) {
            final A found = findAnnotationIncludingMeta(clazz, annotationType, new HashSet<>());
            if (found != null) return found;
        }

        return null;
    }

    public void initialize() {
        final BeanScanner beanScanner = new BeanScanner("com.diy.framework", basePackage);
        beanScanner.scanClassesTypeAnnotatedWith(Component.class).forEach(this::registerBean);
        createBeans();

        initApplicationObjectSupport();

        createWebServer();
    }

    private void registerBean(final Class<?> beanClass) {
        this.beanDefinitionRegistry.add(new AnnotatedGenericBeanDefinition(beanClass));
        postProcessBeanDefinitionRegistry(beanClass);
    }

    private void createBeans() {
        beanDefinitionRegistry.forEach(beanDefinition -> {
            final String beanName = beanDefinition.getBeanName();

            if (isBeanInitialized(beanName)) {
                return;
            }

            createInstance(beanDefinition);
        });
    }

    private void postProcessBeanDefinitionRegistry(final Class<?> beanClass) {
        Arrays.stream(beanClass.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanDefinitionRegistry.add(new ConfigurationClassBeanDefinition(method, beanClass.getSimpleName())));
    }

    private Object createInstance(final BeanDefinition beanDefinition) {
        final Executable factoryMethod = beanDefinition.getFactoryMethod();

        try {
            factoryMethod.setAccessible(true);

            final Object[] arguments = resolveBeanArguments(beanDefinition.getArgumentTypes());

            if (beanDefinition.getFactoryBeanName() == null) {
                final Object bean = autowireConstructor((Constructor<?>) factoryMethod, arguments);
                saveBean(beanDefinition.getBeanName(), bean);

                return bean;
            }

            final Object bean = instantiateUsingFactoryMethod(beanDefinition, arguments);
            saveBean(beanDefinition.getBeanName(), bean);

            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            factoryMethod.setAccessible(false);
        }
    }

    private Object[] resolveBeanArguments(final List<Class<?>> argumentsType) {
        return argumentsType.stream()
                .map(argumentType -> beanDefinitionRegistry.stream()
                        .filter(definition -> definition.getBeanClass().equals(argumentType))
                        .findFirst()
                        .get())
                .map(beanDefinition -> {
                    if (isBeanInitialized(beanDefinition.getBeanName())) {
                        return getBean(beanDefinition.getBeanName());
                    }

                    return createInstance(beanDefinition);
                }).toArray();
    }

    private Object instantiateUsingFactoryMethod(final BeanDefinition beanDefinition, final Object[] arguments) throws InvocationTargetException, IllegalAccessException {
        if (!(beanDefinition instanceof ConfigurationClassBeanDefinition)) {
            throw new RuntimeException("required ConfigurationClassBeanDefinition.");
        }

        return ((Method) beanDefinition.getFactoryMethod()).invoke(getFactoryBean(beanDefinition), arguments);
    }

    private Object getFactoryBean(final BeanDefinition beanDefinition) {
        if (isBeanInitialized(beanDefinition.getFactoryBeanName())) {
            return getBean(beanDefinition.getFactoryBeanName());
        }

        final BeanDefinition factoryBeanDefinition = beanDefinitionRegistry.stream()
                .filter(definition -> definition.getBeanName().equals(beanDefinition.getFactoryBeanName()))
                .findFirst().get();

        return createInstance(factoryBeanDefinition);
    }

    private Object autowireConstructor(final Constructor<?> constructor, final Object[] arguments) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return constructor.newInstance(arguments);
    }

    private boolean isBeanInitialized(final String beanName) {
        return beans.containsKey(beanName);
    }

    private void saveBean(final String beanName, final Object bean) {
        beans.put(beanName, bean);
        beanDefinitionNames.add(beanName);

        mapToSuperTypes(bean.getClass())
                .forEach(clazz -> allBeanNamesByType.computeIfAbsent(clazz, beanType -> new HashSet<>())
                        .add(beanName));
    }

    private Set<Class<?>> mapToSuperTypes(final Class<?> clazz) {
        final HashSet<Class<?>> superTypes = new HashSet<>();
        Class<?> superClass = clazz;

        while (superClass != null) {
            final Class<?>[] interfaces = superClass.getInterfaces();
            superTypes.add(superClass);
            superTypes.addAll(List.of(interfaces));

            superClass = superClass.getSuperclass();
        }

        return superTypes;
    }

    private void initApplicationObjectSupport() {
        final Map<String, ApplicationObjectSupport> supports = getBeansOfType(ApplicationObjectSupport.class);
        supports.values().forEach(support -> support.setApplicationContext(this));
    }

    private void createWebServer() {
        final WebServer webServer = new TomcatWebServer(getSelfInitializer());
        webServer.start();
    }

    private ServletContextInitializer getSelfInitializer() {
        return this::selfInitialize;
    }

    private void selfInitialize(final ServletContext servletContext) {
        prepareWebApplicationContext(servletContext);
    }

    private void prepareWebApplicationContext(final ServletContext servletContext) {
        servletContext.setAttribute(ApplicationContext.APPLICATION_CONTEXT_ATTRIBUTE, this);
    }

    private <A extends Annotation> A findAnnotationIncludingMeta(final Class<?> element,
                                                                 final Class<A> target,
                                                                 final Set<Class<? extends Annotation>> visited) {
        final A direct = element.getAnnotation(target);
        if (direct != null) return direct;
        for (final Annotation ann : element.getAnnotations()) {
            final Class<? extends Annotation> annType = ann.annotationType();
            if (!visited.add(annType)) continue;
            final A found = findAnnotationIncludingMeta(annType, target, visited);
            if (found != null) return found;
        }
        return null;
    }

}
