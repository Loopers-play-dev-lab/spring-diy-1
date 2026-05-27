package com.diy.framework.web.mvc;

import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerMethod {
    private final Object bean;      // 컨트롤러 인스턴스
    private final Method method;    // 실행할 메서드

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public ModelAndView invoke(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] == HttpServletRequest.class) {
                args[i] = req;
            } else if (paramTypes[i] == HttpServletResponse.class) {
                args[i] = resp;
            }
        }

        method.setAccessible(true);
        return (ModelAndView) method.invoke(bean, args);
    }
}