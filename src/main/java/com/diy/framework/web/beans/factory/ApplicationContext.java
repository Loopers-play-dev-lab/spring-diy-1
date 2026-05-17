package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.RequestMapping;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    public ApplicationContext(final String... basePackages) {
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry();
        BeanDefinitionReader definitionReader = new BeanDefinitionReader(new BeanScanner(basePackages), registry);
        definitionReader.loadBeanDefinitions();

        this.beanFactory = new BeanFactory(registry);
        beanFactory.preInstantiateSingletons();
    }

    public void initialize() {
        final DispatcherServlet servlet = new DispatcherServlet(resolveControllerMappings());
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }

    private Map<String, Controller> resolveControllerMappings() {
        Map<String, Controller> controllerMappings = new LinkedHashMap<>();

        for (Controller controller : getBeansOfType(Controller.class).values()) {
            RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                throw new IllegalStateException("컨트롤러에 @RequestMapping이 없습니다: " + controller.getClass().getName());
            }

            if (controllerMappings.containsKey(requestMapping.value())) {
                throw new IllegalStateException("같은 경로의 컨트롤러가 이미 등록되어 있습니다: " + requestMapping.value());
            }

            controllerMappings.put(requestMapping.value(), controller);
        }

        return controllerMappings;
    }

    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Object getBean(final String name) {
        return beanFactory.getBean(name);
    }

    public <T> Map<String, T> getBeansOfType(final Class<T> type) {
        return beanFactory.getBeansOfType(type);
    }
}
