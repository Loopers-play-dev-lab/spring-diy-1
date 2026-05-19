package com.diy.framework.web;

import com.diy.framework.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object instance; //lectureController 인터페이스
    private final Method method; //list() 메서드 객체

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res) throws Exception { //호출
        return (ModelAndView) method.invoke(instance, req, res);
    }
}
