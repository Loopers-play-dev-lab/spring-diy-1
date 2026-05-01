package com.diy.framework.web.mvc.view;

public class UrlBasedViewResolver implements ViewResolver {
    private final String REDIRECT_PREFIX = "redirect:";

    @Override
    public View resolveViewName(String viewName) {
        return new RedirectView(viewName.replace(REDIRECT_PREFIX, ""));
    }
}