package com.diy.app.utils;

import com.diy.app.annotation.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HandlerMapping {
    private final Map<String, Object> handlerMap = new HashMap<>();

    public void initalize() {
        List<Object> controllers = ApplicationContext.getBeansByAnnotation(com.diy.app.annotation.Controller.class);
        System.out.println(controllers);
        for(Object controller : controllers) {
            Class<?> clazz = controller.getClass();
            String prefix = "";
            if(clazz.isAnnotationPresent(RequestMapping.class)) {
                prefix = clazz.getAnnotation(RequestMapping.class).value();
            }
            Method[] methods = clazz.getDeclaredMethods();
            for(Method method : methods) {
                if(method.isAnnotationPresent(GetMapping.class)) {
                    register(controller,method,"GET",prefix,method.getAnnotation(GetMapping.class).value());
                } else if(method.isAnnotationPresent(PostMapping.class)) {
                    register(controller,method,"POST",prefix,method.getAnnotation(PostMapping.class).value());
                } else if(method.isAnnotationPresent(PutMapping.class)) {
                    register(controller,method,"PUT",prefix,method.getAnnotation(PutMapping.class).value());
                } else if (method.isAnnotationPresent(DelMapping.class)) {
                    register(controller,method,"DELETE",prefix,method.getAnnotation(DelMapping.class).value());
                }
            }

        }
    }
    private void register(Object controller, Method method, String httpMethod, String prefix, String suffix) {
        String fullPath = combinePath(prefix, suffix);
        if(fullPath.length() > 1  && fullPath.endsWith("/")) {
            fullPath = fullPath.substring(0, fullPath.length() - 1);
        }
        String key = httpMethod + ":" + fullPath;
        handlerMap.put(key,new HandlerExecution(controller,method));
    }

    public HandlerExecution getHandler(String method, String uri) {
        return (HandlerExecution) handlerMap.get(method.toUpperCase()+":" + uri);
    }
    private String combinePath(String prefix, String subPath) {
        // 모든 경로 조각에서 앞뒤 슬래시를 제거한 뒤 다시 "/"로 조립
        String p1 = prefix.replaceAll("^/|/$", "");
        String p2 = subPath.replaceAll("^/|/$", "");
        return "/" + (p1.isEmpty() ? p2 : p1 + "/" + p2);
    }

}
