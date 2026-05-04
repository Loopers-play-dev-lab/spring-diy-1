package com.diy.framework.web.view;

public class ViewResolver {
    private final String prefix;
    private final String suffix;

    public ViewResolver(final String prefix, final String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public View resolve(final String viewName) {
        final String fullPath = prefix + viewName + suffix;
        if (suffix.equals(".jsp")) { //확장자jsp인 경우
            return new JspView(fullPath);
        }
        return new HtmlView(fullPath);
    }
}
