package com.diy.framework.web.mvc.view;

public class HtmlViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(final String viewName) {
        final String resolvedViewName = "/templates/" + viewName + ".html";
        return new HtmlView(resolvedViewName);
    }
}
