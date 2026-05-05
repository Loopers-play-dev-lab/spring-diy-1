package com.diy.framework.web.mvc.view;

public class JspViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(final String viewName) {
        return new JspView("/" + viewName + ".jsp");
    }
}
