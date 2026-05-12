package com.diy.framework.web.beans.factory;

public class BeanContainer {

    private final BeanFactory beanFactory;

    public BeanContainer(final String... basePackages) {
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry();
        BeanDefinitionReader definitionReader = new BeanDefinitionReader(new BeanScanner(basePackages), registry);
        definitionReader.loadBeanDefinitions();

        this.beanFactory = new BeanFactory(registry, new ConstructorResolver());
        beanFactory.preInstantiateSingletons();
    }

    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Object getBean(final String name) {
        return beanFactory.getBean(name);
    }
}
