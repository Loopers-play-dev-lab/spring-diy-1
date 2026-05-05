package com.diy.framework.web.utils;

import java.util.Map;

public class ResponseV1 {
    private final String viewName;
    private final Model model;

    public ResponseV1(String viewName) {
        this.viewName = viewName;
        this.model = new Model();
    }

    public ResponseV1(String viewName, Model model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModels() {
        return this.model.getModels();
    }


}
