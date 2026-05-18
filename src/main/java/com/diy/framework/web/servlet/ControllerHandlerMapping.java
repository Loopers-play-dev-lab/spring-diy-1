package com.diy.framework.web.servlet;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;

public class ControllerHandlerMapping {

    private final Map<String, Controller> handlerMap = new HashMap<>();

    public ControllerHandlerMapping(ApplicationContext context) {
        context.getBeanNames().forEach(name -> {
            Object bean = context.getBean(name);
            if (bean instanceof Controller controller && bean.getClass().isAnnotationPresent(RequestMapping.class)) {
                String url = bean.getClass().getAnnotation(RequestMapping.class).value();
                handlerMap.put(url, controller);
            }
        });
    }

    public Controller getHandler(String url) {
        return handlerMap.get(url);
    }
}
