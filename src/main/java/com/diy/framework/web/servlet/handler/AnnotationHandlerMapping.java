package com.diy.framework.web.servlet.handler;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping {

    private final Map<HandlerKey, Method> handlerMap = new HashMap<>();

    public AnnotationHandlerMapping(ApplicationContext context) {
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

                            handlerMap.put(handlerKey, method);
                        });
                    });
        });
    }

    public Method getHandler(HttpServletRequest request) {
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
            return clazz.getAnnotation(RequestMapping.class).methods();
        }

        return new RequestMethod[0];
    }
}
