package com.diy.framework.web.utils;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> models = new HashMap<String, Object>();

    public Model() {
    }

    public void addModel(String key, Object model) {
        models.put(key, model);
    }

    public Map<String, Object> getModels() {
        return models;
    }
}
