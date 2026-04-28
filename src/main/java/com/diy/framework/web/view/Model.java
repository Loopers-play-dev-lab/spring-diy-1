package com.diy.framework.web.view;

import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;

public class Model {
    private final ConcurrentHashMap<String, Object> datas = new ConcurrentHashMap<>();

    public void addAttribute(String key, Object value) {
        this.datas.put(key, value);
    }

    public void addToView(HttpServletRequest servletRequest){
        datas.forEach(servletRequest::setAttribute);
    }
}
