package com.diy.app.lecture.controller;

import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.handler.HandlerMapping;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/")
public class LecturesDispatcherServlet extends DispatcherServlet {

    @Override
    protected String[] getBasePackages() {
        return new String[]{"com.diy"};
    }

    @Override
    protected void initHandlerMappings(final HandlerMapping handlerMapping)
            throws InvocationTargetException, IllegalAccessException {
    }
}
