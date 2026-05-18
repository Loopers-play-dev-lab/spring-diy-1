package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.context.ApplicationContext;
import com.diy.framework.web.mvc.annotation.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Map<String, HandlerMethod> handlers = new HashMap<>();
    private final ApplicationContext applicationContext;

    public AnnotationHandlerMapping(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        for (Object bean : applicationContext.getAllBeans().values()) {
            Class<?> clazz = bean.getClass();

            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String basePath = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                basePath = clazz.getAnnotation(RequestMapping.class).value();
            }

            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                String path = basePath + mapping.value();
                RequestMethod[] methods = mapping.methods();

                for (RequestMethod requestMethod : methods) {
                    String key = requestMethod.name() + " " + path;
                    handlers.put(key, new HandlerMethod(bean, method));
                }
            }
        }
    }

    @Override
    public Object getHandler(String key) {
        return handlers.get(key);
    }
}
