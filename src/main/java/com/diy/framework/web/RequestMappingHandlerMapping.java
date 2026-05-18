package com.diy.framework.web;

import com.diy.framework.web.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

public class RequestMappingHandlerMapping {
    private final Map<String, Controller> handlerMap = new HashMap<>();

    public RequestMappingHandlerMapping(Map<String, Object> beans) {
        for (Object bean : beans.values()) {
            Class<?> clazz = bean.getClass();
            if (!clazz.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            if (!(bean instanceof Controller)) {
                throw new RuntimeException(clazz.getName() + "Controller 인터페이스의 구현체가 아니면 @RequestMapping을 사용할 수 없습니다.");
            }

            String url = clazz.getAnnotation(RequestMapping.class).value();
            if (url.isEmpty()) {
                throw new RuntimeException(clazz.getName() + "의 @RequestMapping value가 비어있습니다.");
            }

            if (handlerMap.containsKey(url)) {
                throw new RuntimeException("URL: " + url + "에 매핑된 컨트롤러가 이미 존재합니다: " + clazz.getName());
            }

            handlerMap.put(url, (Controller) bean);
        }
    }

    public Controller getController(String url) {
        return handlerMap.get(url);
    }
}
