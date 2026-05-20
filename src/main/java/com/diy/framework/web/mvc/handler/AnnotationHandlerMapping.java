package com.diy.framework.web.mvc.handler;

import com.diy.framework.context.annotation.Controller;
import com.diy.framework.context.annotation.RequestMapping;
import com.diy.framework.web.mvc.HandlerKey;
import com.diy.framework.web.mvc.HandlerMapping;
import com.diy.framework.web.mvc.HandlerMethod;
import com.diy.framework.web.mvc.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Map<HandlerKey, HandlerMethod> handlerMap = new HashMap<>();

    public void initialize(final Collection<Object> beans) {
        beans.stream()
                .filter(bean -> bean.getClass().isAnnotationPresent(Controller.class))
                .forEach(this::registerHandler);
    }

    private void registerHandler(final Object bean) {
        final String baseUrl = getBaseUrl(bean.getClass());

        for (final Method method : bean.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            final RequestMapping rm = method.getAnnotation(RequestMapping.class);
            final String url = baseUrl + rm.value();

            for (final RequestMethod httpMethod : rm.methods()) {
                handlerMap.put(new HandlerKey(url, httpMethod), new HandlerMethod(bean, method));
            }
        }
    }

    private String getBaseUrl(final Class<?> clazz) {
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            return clazz.getAnnotation(RequestMapping.class).value();
        }
        return "";
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());
        return handlerMap.get(new HandlerKey(request.getRequestURI(), method));
    }
}
