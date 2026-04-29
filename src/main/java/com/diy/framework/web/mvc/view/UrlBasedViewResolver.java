package com.diy.framework.web.mvc.view;

public class UrlBasedViewResolver implements ViewResolver{

    private static final UrlBasedViewResolver instance = new UrlBasedViewResolver();

    private UrlBasedViewResolver() {}

    public static UrlBasedViewResolver getInstance() {
        return instance;
    }
    @Override
    public View resolveView(String viewName) {
        if (!viewName.startsWith("redirect:")) {
            return null;
        }
        return new RedirectView(viewName.substring("redirect:".length()));
    }
}
