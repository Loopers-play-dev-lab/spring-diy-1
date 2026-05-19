package com.diy.framework.web.servlet.handler.mapping;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.Controller;
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
            if (bean instanceof Controller controller) {
                if (!name.startsWith("/")) {
                    throw new RuntimeException("Controller bean name does not start with '/'");
                }

                handlerMap.put(name, controller);
            }
        });
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMap.get(requestURI);
    }
}
