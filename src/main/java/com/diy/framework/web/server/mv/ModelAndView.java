package com.diy.framework.web.server.mv;

import java.util.Map;

public class ModelAndView {
    private final String viewName;
    private Model model = new Model();

    public ModelAndView(final String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(final String viewName, final Model model) {
        this.viewName = viewName;
        this.model = model;
    }

    public ModelAndView(final String viewName, final Map<String, Object> attributes) {
        this.viewName = viewName;
        this.model.setAttributes(attributes);
    }

    public String getViewName() {
        return viewName;
    }

    public Model getModel() {
        return model;
    }

    public Map<String, Object> getAttributes() {
        return model.getAttributes();
    }
}
