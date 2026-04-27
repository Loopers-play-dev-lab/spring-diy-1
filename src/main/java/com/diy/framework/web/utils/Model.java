package com.diy.framework.web.utils;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private String viewName;
    private Map<String, Object> models = new HashMap<String, Object>();

    public Model(String viewName) {
        this.viewName = viewName;
    }

    public void addModel(String key, Object model) {
        models.put(key, model);
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModels() {
        return models;
    }
}
