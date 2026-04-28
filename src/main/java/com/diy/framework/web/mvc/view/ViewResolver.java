package com.diy.framework.web.mvc.view;

import javax.servlet.ServletContext;

public class ViewResolver {
    private final String prefix;
    private final String suffix;

    public ViewResolver(final String prefix, final String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public View resolve(final String viewName, final ServletContext servletContext) {
        String fullPath = prefix + viewName + suffix;
        if (servletContext.getRealPath(fullPath) == null) {
            return null;
        }

        if (suffix.equals(".jsp")) {
            return new JspView(fullPath);
        }

        return new HtmlView(fullPath);
    }
}
