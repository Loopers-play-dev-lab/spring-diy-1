package com.diy.framework.web.servlet.handler;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.Controller;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class ControllerHandlerMapping extends AbstractHandlerMapping {

    private final Map<String, Controller> handlerMap = new HashMap<>();

    public ControllerHandlerMapping(ApplicationContext context) {
        setOrder(2);
        context.getBeanNames().forEach(name -> {
            Object bean = context.getBean(name);
            if (bean instanceof Controller controller) {
                if (!name.startsWith("/")) {
                    throw new RuntimeException("Controller bean name must start with '/'");
                }
                handlerMap.put(name, controller);
            }
        });
    }

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) {
        return handlerMap.get(request.getRequestURI());
    }
}
