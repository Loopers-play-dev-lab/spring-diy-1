package com.diy.framework.view;

public class HtmlViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(final String viewName) {
        String resolvedViewName = String.format("/templates/%s.html", viewName);
        return new HtmlView(resolvedViewName);
    }
}
