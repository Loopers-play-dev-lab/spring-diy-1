package com.diy.app.lecture.controller;

import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.HandlerMapping;
import com.diy.framework.web.beans.factory.BeanFactory;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/")
public class LecturesDispatcherServlet extends DispatcherServlet {

    @Override
    protected void initHandlerMappings(final HandlerMapping handlerMapping)
            throws InvocationTargetException, IllegalAccessException {
        BeanFactory beanFactory = new BeanFactory("com.diy");

        handlerMapping.setMapping("GET", "/lectures", beanFactory.getBean(LectureListController.class));
        handlerMapping.setMapping("POST", "/lectures", beanFactory.getBean(LectureCreateController.class));
        handlerMapping.setMapping("PUT", "/lectures", beanFactory.getBean(LectureUpdateController.class));
        handlerMapping.setMapping("DELETE", "/lectures", beanFactory.getBean(LectureDeleteController.class));
    }
}
