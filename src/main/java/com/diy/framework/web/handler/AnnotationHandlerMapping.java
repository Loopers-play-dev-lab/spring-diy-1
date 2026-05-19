package com.diy.framework.web.handler;

import com.diy.framework.web.annotation.Controller;
import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.annotation.RequestMethod;
import com.diy.framework.web.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private final Map<String, HandlerExecution> mappings = new HashMap<>();

    public AnnotationHandlerMapping(BeanFactory beanFactory) {
        initialize(beanFactory);
    }

    private void initialize(BeanFactory beanFactory) {
        beanFactory.getBeans().forEach((clazz, bean) -> {
            if (!clazz.isAnnotationPresent(Controller.class)) {
                return;
            }

            String baseUri = clazz.isAnnotationPresent(RequestMapping.class)
                    ? clazz.getAnnotation(RequestMapping.class).value()
                    : "";

            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                String uri = baseUri + rm.value();
                for (RequestMethod httpMethod : rm.methods()) {
                    mappings.put(httpMethod.name() + ":" + uri, new HandlerExecution(bean, method));
                }
            }
        });
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        return mappings.get(request.getMethod() + ":" + request.getRequestURI());
    }
}
