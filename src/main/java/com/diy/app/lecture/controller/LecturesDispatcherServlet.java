package com.diy.app.lecture.controller;

import com.diy.framework.web.Controller;
import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.HandlerMapping;
import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.beans.factory.BeanFactory;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/")
public class LecturesDispatcherServlet extends DispatcherServlet {

    @Override
    protected void initHandlerMappings(final HandlerMapping handlerMapping)
            throws InvocationTargetException, IllegalAccessException {
        BeanFactory beanFactory = new BeanFactory("com.diy");

        //자동 매핑- @RequestMapping이 붙은 Controller 빈을 스캔하여 등록
        beanFactory.getBeans().forEach((clazz, bean) -> {
            if (bean instanceof Controller && clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping rm = clazz.getAnnotation(RequestMapping.class);
                handlerMapping.setMapping(rm.method(), rm.uri(), (Controller) bean);
            }
        });
    }
}
