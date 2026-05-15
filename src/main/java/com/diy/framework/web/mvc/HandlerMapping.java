package com.diy.framework.web.mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private static final HandlerMapping INSTANCE = new HandlerMapping();

    private final Map<String, Controller> handlers = new HashMap<>();

    public static HandlerMapping getInstance() {
        return INSTANCE;
    }

    public void register(String path, Controller controller) {
        handlers.put(path, controller);
    }

    public Controller getHandler(HttpServletRequest request) {
        return handlers.get(request.getRequestURI());
    }
}
