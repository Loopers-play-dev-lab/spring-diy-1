package com.diy.framework.web.view;

public class JspViewResolver implements ViewResolver {
    private static final String VIEW_EXTENSION = ".jsp";
    private static JspViewResolver instance;
    private JspViewResolver() {}
    public static JspViewResolver getInstance() {
        if (instance == null) {
            instance = new JspViewResolver();
        }
        return instance;
    }
    public View resolveView(String viewName) {
        if (viewName.contains("redirect:")) {
            return new JspView(viewName);
        }
        if (viewName.isEmpty()) {
            return new JspView(viewName);
        }
        return new JspView(viewName + VIEW_EXTENSION);
    }

}
