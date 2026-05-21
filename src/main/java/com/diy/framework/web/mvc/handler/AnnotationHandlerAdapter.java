package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.ModelAndView;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.Map;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    @Override
    public ModelAndView handle(Object handler, Map<String, ?> params) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Object[] args = resolveArguments(method, params);
        return (ModelAndView) method.invoke(handlerMethod.getBean(), args);
    }

    private Object[] resolveArguments(Method method, Map<String, ?> params) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            String name = parameters[i].getName();
            Class<?> type = parameters[i].getType();
            Object value = params.get(name);

            if (value instanceof String[]) {
                value = ((String[]) value)[0];
            }

            args[i] = convertType((String) value, type);
        }

        return args;
    }

    private Object convertType(String value, Class<?> type) {
        if (value == null) {
            return null;
        }
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        }
        if (type == BigDecimal.class) {
            return new BigDecimal(value);
        }
        return value;
    }
}
