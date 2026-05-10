package com.diy.framework.web.server;

public class JspViewResolver implements ViewResolver {
    @Override
    public View resolve(final String viewName) {
        return new JspView("/" + viewName + ".jsp");
    }
}
