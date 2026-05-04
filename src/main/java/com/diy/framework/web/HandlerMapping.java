package com.diy.framework.web;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private final Map<String, Controller> mappings = new HashMap<>();

    public void setMapping(String method, String uri, Controller controller) {
        mappings.put(method + ":" + uri, controller);
    }

    public Controller getController(HttpServletRequest request) {
        return mappings.get(request.getMethod() + ":" + request.getRequestURI());
    }
}
