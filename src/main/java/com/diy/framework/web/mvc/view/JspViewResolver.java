package com.diy.framework.web.mvc.view;

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
        return new JspView(viewName + VIEW_EXTENSION);
    }

}
