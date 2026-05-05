package com.diy.app.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private String viewName;
    private final Map<String, Object> model = new HashMap<>();

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        if (model != null) {
            this.model.putAll(model);
        }
    }

    public void addObject(String key, Object value) {
        model.put(key, value);
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }
}
