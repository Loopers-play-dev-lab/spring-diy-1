package com.diy.framework.web;

import java.util.HashMap;
import java.util.Map;

public class RequestBody {
    Map<String, ?> map;

    private RequestBody(Map<String, ?> map) {
        this.map = map;
    }

    public static RequestBody of(Map<String, ?> map) {
        return new RequestBody(map);
    }

    public Object get(String key) {
        return map.get(key);
    }
}
