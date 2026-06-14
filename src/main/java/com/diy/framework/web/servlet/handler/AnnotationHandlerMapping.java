package com.diy.framework.web.servlet.handler;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.annotation.RequestMethod;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping extends AbstractHandlerMapping {

    private final Map<RequestMappingKey, HandlerMethod> handlerMethods = new HashMap<>();

    public AnnotationHandlerMapping(ApplicationContext context) {
        setOrder(0);
        Arrays.stream(context.getBeanNamesForAnnotation(Controller.class))
                .forEach(name -> detectHandlerMethods(context.getBean(name)));
    }

    private void detectHandlerMethods(Object bean) {
        Class<?> clazz = bean.getClass();
        String urlPrefix = resolveUrlPrefix(clazz);

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    String url = urlPrefix + annotation.value();
                    RequestMappingKey key = new RequestMappingKey(url,
                            new RequestMethodsRequestCondition(resolveMethods(clazz, annotation)));

                    if (handlerMethods.containsKey(key)) {
                        throw new RuntimeException("Ambiguous mapping for: " + url);
                    }

                    handlerMethods.put(key, new HandlerMethod(bean, method));
                });
    }

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) {
        return handlerMethods.entrySet().stream()
                .filter(entry -> entry.getKey().matches(request))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
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
