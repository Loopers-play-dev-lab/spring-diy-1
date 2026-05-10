package com.diy.framework.web.server.mv;

public class HtmlViewResolver implements ViewResolver {
    @Override
    public View resolve(final String viewName) {
        return new HtmlView("/" + viewName + ".html");
    }
}
