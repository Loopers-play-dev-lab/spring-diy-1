package com.diy.framework.web.view;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> attributes = new HashMap<>();

    public Model setAttribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
