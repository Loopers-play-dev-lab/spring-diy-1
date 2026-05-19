package com.diy.framework.web.servlet.handler;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object bean;
    private final Method method;

    public HandlerExecution(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }
}
