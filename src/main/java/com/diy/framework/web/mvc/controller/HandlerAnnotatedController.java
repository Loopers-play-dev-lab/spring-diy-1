package com.diy.framework.web.mvc.controller;

import com.diy.framework.context.annotation.RequestMapping;
import com.diy.framework.context.annotation.RequestMethod;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AnnotatedControllerDefinition implements ControllerDefinition {
    private final String url;
    private final Object controller;

    public AnnotatedControllerDefinition(String url, Object controller) {
        this.controller = controller;
        this.url = url;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        String methodName = request.getMethod();
        Optional<Method> requestMethod = Arrays.stream(controller.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(method -> {
                    RequestMethod[] requestMethods = method.getAnnotation(RequestMapping.class).methods();
                    return Arrays.stream(requestMethods).anyMatch(method1 -> method1.equals(methodName));
                })
                .findFirst();

        Method method = requestMethod.orElseThrow(() -> new RuntimeException("Not found Exception"));

        return (ModelAndView) method.invoke(controller, request, response);
    }

    public String getUrl() {
        return url;
    }
}
