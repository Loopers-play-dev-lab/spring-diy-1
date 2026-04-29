package com.diy.framework.web.model;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private final Map<String, Object> model = new HashMap<>();

    public Map<String, Object> getModel() {
        return model;
    }

    public void addAttribute(String name, Object models) {
        model.put(name, models);
    }
}
