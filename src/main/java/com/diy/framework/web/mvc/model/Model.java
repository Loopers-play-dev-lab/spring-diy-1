package com.diy.framework.web.mvc.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> modelMap;

    public Model() {
        modelMap = new HashMap<>();
    }

    public void addAttribute(String key, Object value) {
        modelMap.put(key, value);
    }

    public Map<String, Object> getAttribute() {
        return Collections.unmodifiableMap(modelMap);
    }
}
