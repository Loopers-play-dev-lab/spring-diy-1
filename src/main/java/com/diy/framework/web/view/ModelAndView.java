package com.diy.framework.web.view;

public class ModelAndView {
    private final String viewName;

    public ModelAndView(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
