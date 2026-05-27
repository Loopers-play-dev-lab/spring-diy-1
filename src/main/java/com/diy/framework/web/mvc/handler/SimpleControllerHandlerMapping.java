package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.context.ApplicationContext;
import com.diy.framework.web.mvc.controller.Controller;

import java.util.HashMap;
import java.util.Map;

public class SimpleControllerHandlerMapping implements HandlerMapping {
    private final Map<String, Controller> controllers = new HashMap<>();
    private final ApplicationContext applicationContext;

    public SimpleControllerHandlerMapping(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        for (Map.Entry<String, Object> entry : applicationContext.getAllBeans().entrySet()) {
            Object bean = entry.getValue();
            if (bean instanceof Controller) {
                // 빈 이름을 그대로 매핑 키로 사용
                controllers.put(entry.getKey(), (Controller) bean);
            }
        }
    }

    public void put(String key, Controller controller) {
        controllers.put(key, controller);
    }

    @Override
    public Object getHandler(String key) {
        // DispatcherServlet 은 "GET /lectures" 처럼 "METHOD path" 로 조회하지만,
        // 인터페이스 매핑은 path(URL)만 키로 가진다. HTTP Method 를 떼고 path 로만 조회한다.
        String path = key.contains(" ") ? key.split(" ", 2)[1] : key;
        return controllers.get(path);
    }
}
