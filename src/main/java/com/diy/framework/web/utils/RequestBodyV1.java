package com.diy.framework.web.utils;

import java.util.Map;

public class RequestBodyV1 {
    Map<String, ?> map;

    private RequestBodyV1(Map<String, ?> map) {
        this.map = map;
    }

    public static RequestBodyV1 of(Map<String, ?> map) {
        return new RequestBodyV1(map);
    }

    public Object get(String key) {
        return map.get(key);
    }
}
