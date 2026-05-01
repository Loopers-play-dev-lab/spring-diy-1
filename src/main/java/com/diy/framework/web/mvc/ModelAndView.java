package com.diy.framework.web.mvc;

import com.diy.framework.web.mvc.model.Model;

import java.util.Map;

public class ModelAndView {
    private final String viewName;
    private final Model model;

    public ModelAndView(final String viewName) {
        this.viewName = viewName;
        this.model = new Model();
    }

    public ModelAndView(final String viewName, final Model model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return model.getAttribute();
    }
}
