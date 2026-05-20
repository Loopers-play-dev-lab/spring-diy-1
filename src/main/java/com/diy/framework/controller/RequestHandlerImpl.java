package com.diy.framework.controller;

import com.diy.framework.bean.ApplicationContext;
import com.diy.framework.bean.BeanScanner;
import com.diy.framework.bean.annotation.Controller;
import com.diy.framework.bean.annotation.RequestMapping;
import com.diy.framework.enums.RequestMethod;
import com.diy.framework.value.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RequestHandlerImpl implements RequestHandler {

    private final ApplicationContext applicationContext;

    private final Set<Class<?>> controllers = new HashSet<>();
    public static final Set<RequestKey> keys = new HashSet<>();
    public static final Map<RequestKey, HandleMethod> handlers = new HashMap<>();

    BeanScanner beanScanner = new BeanScanner();

    public RequestHandlerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.controllers.addAll(beanScanner.scanClassesTypeAnnotatedWith(Controller.class));
    }

    public void registerRequestMapping() {
        Set<Method> methodsInner = beanScanner.scanMethodsAnnotatedWith(RequestMapping.class);

        for (Method method : methodsInner) {
            if (!controllers.contains(method.getDeclaringClass())){
                throw new RuntimeException("Illegal method is injected");
            }

            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            Object bean = applicationContext.getBean(method.getDeclaringClass());
            RequestMethod[] requestMethods = requestMapping.method();

            for (RequestMethod requestMethod : requestMethods) {
                RequestKey key = new RequestKey(requestMethod.name(), requestMethod);
                handlers.put(key, new HandleMethod(key, bean, method));
            }
        }
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestKey key = new RequestKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));

        HandleMethod handle = handlers.get(key);
        if (handle == null) {
            throw new RuntimeException("404 Not Found");
        }

        return (ModelAndView) handle.method().invoke(handle.bean(), request, response);
    }
}
