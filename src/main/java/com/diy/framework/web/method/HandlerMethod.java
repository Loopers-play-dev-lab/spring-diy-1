package com.diy.framework.web.method;

import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HandlerMethod {

    private final Object bean;
    private final Method method;
    private final ObjectMapper mapper;

    public HandlerMethod(final Object bean, final Method method) {
        this.bean = bean;
        this.method = method;
        this.mapper = new ObjectMapper();
    }

    public Method getMethod() {
        return method;
    }

    public Object invokeForRequest(final HttpServletRequest req,
                                   final HttpServletResponse res,
                                   final HandlerMethodArgumentResolverComposite argumentResolvers) throws Exception {
        try {
            method.setAccessible(true);

            Map<String, Object> bodyMap = new HashMap<>();
            if (req.getMethod().equals("POST") || req.getMethod().equals("PUT")) {
                bodyMap = mapper.readValue(req.getInputStream(), Map.class);
            }

            Parameter[] parameterArrays = method.getParameters();
            Class<?>[] parameterTypes = method.getParameterTypes();
            ArrayList<String> parameterNames = Collections.list(req.getParameterNames());
            final Object[] parameters = new Object[parameterArrays.length];

            for (int i = 0; i < parameterArrays.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (ServletRequest.class.isAssignableFrom(parameterType)) {
                    parameters[i] = req;
                    continue;
                }

                if (ServletResponse.class.isAssignableFrom(parameterType)) {
                    parameters[i] = res;
                    continue;
                }

                if (req.getMethod().equals("GET") || req.getMethod().equals("DELETE")) {
                    parameters[i] = req.getParameter(parameterNames.get(i));
                    continue;
                }

                if (!bodyMap.isEmpty()) {
                    if (parameterType == String.class || parameterType.isPrimitive() || Number.class.isAssignableFrom(parameterType)) {
                        parameters[i] = bodyMap.get(parameterArrays[i].getName());
                    }
                    else mapper.convertValue(bodyMap, parameterType);
                }
            }

            final Object view = this.method.invoke(bean, parameters);
            req.setAttribute(method.getReturnType().getSimpleName(), view);

            final Map<String, Object> model = new HashMap<>();

            req.getAttributeNames().asIterator().forEachRemaining(name -> {
                Object value = req.getAttribute(name);
                model.put(name, value);
            });

            return method.invoke(bean, args);
        } finally {
            method.setAccessible(false);
        }
    }
}
