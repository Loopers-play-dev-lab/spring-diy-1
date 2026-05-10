package com.diy.framework.web.server.mv;

public class HtmlViewResolver implements ViewResolver {
    @Override
    public View resolve(final String viewName) {
        if (viewName == null || viewName.isBlank()) {
            return null;
        }
        return new HtmlView("/" + viewName + ".html");
    }
}
