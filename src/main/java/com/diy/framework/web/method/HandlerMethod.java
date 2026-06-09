package com.diy.framework.web.method;

import com.diy.framework.web.method.support.HandlerMethodArgumentResolverComposite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerMethod {

    private final Object bean;
    private final Method method;

    public HandlerMethod(final Object bean, final Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public Object invokeForRequest(final HttpServletRequest req,
                                   final HttpServletResponse res,
                                   final HandlerMethodArgumentResolverComposite argumentResolvers) throws Exception {
        try {
            method.setAccessible(true);

            final Parameter[] parameters = method.getParameters();
            final Object[] args = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                args[i] = argumentResolvers.resolveArgument(parameters[i], req, res);
            }

            return method.invoke(bean, args);
        } finally {
            method.setAccessible(false);
        }
    }
}
