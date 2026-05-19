package com.diy.framework.web.servlet.handler.adapter;

import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.servlet.handler.HandlerExecution;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        Object bean = handlerExecution.getBean();
        Method method = handlerExecution.getMethod();

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
