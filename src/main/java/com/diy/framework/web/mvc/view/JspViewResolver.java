package com.diy.framework.web.mvc.view;

public class JspViewResolver implements ViewResolver {
    private final String JSP_PREFIX = "/";
    private final String JSP_SUFFIX = ".jsp";

    @Override
    public View resolveViewName(String viewName) {
        return new JspView(JSP_PREFIX + viewName + JSP_SUFFIX);
    }
}