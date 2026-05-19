package com.diy.framework.web.servlet.handler.mapping;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class ControllerHandlerMapping implements HandlerMapping {

    private final ApplicationContext context;
    private final Map<String, Controller> handlerMap = new HashMap<>();

    public ControllerHandlerMapping(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void initialize() {
        context.getBeanNames().forEach(name -> {
            Object bean = context.getBean(name);
            if (bean instanceof Controller controller && bean.getClass().isAnnotationPresent(RequestMapping.class)) {
                String url = bean.getClass().getAnnotation(RequestMapping.class).value();
                handlerMap.put(url, controller);
            }
        });
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMap.get(requestURI);
    }
}
