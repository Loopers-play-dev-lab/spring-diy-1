package com.diy.framework.web.method;

import com.diy.framework.web.mvc.view.ModelAndView;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerMethod {

    private final Object bean;
    private final Method method;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] args = Arrays.stream(method.getParameters())
                .map(parameter -> resolveParameter(parameter, request, response))
                .toArray();

        return (ModelAndView) method.invoke(bean, args);
    }

    private Object resolveParameter(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> type = parameter.getType();

        if (type == HttpServletRequest.class) {
            return request;
        }

        if (type == HttpServletResponse.class) {
            return response;
        }

        return request.getParameter(parameter.getName());
    }
}
