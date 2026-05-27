package com.diy.framework.web.servlet.handler.mapping;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.annotation.RequestMethod;
import com.diy.framework.web.servlet.handler.HandlerExecution;
import com.diy.framework.web.servlet.handler.HandlerKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final ApplicationContext context;
    private final Map<HandlerKey, HandlerExecution> handlerMap = new HashMap<>();

    public AnnotationHandlerMapping(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void initialize() {
        context.getBeanNames().forEach(name -> {
            Object bean = context.getBean(name);
            Class<?> clazz = bean.getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                return;
            }

            String urlPrefix = resolveUrlPrefix(clazz);

            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> {
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String url = urlPrefix + annotation.value();
                        RequestMethod[] methods = resolveMethods(clazz, annotation);
                        Arrays.stream(methods).forEach(httpMethod -> {
                            HandlerKey handlerKey = new HandlerKey(url, httpMethod);

                            if (handlerMap.containsKey(handlerKey)) {
                                throw new RuntimeException("Multiple handler mapping");
                            }

                            HandlerExecution handlerExecution = new HandlerExecution(bean, method);
                            handlerMap.put(handlerKey, handlerExecution);
                        });
                    });
        });
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        return handlerMap.get(handlerKey);
    }

    private String resolveUrlPrefix(Class<?> clazz) {
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            return clazz.getAnnotation(RequestMapping.class).value();
        }

        return "";
    }

    private RequestMethod[] resolveMethods(Class<?> clazz, RequestMapping methodAnnotation) {
        if (methodAnnotation.methods().length > 0) {
            return methodAnnotation.methods();
        }

        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classAnnotation = clazz.getAnnotation(RequestMapping.class);
            if (classAnnotation.methods().length > 0) {
                return classAnnotation.methods();
            }
        }

        return RequestMethod.values();
    }
}
