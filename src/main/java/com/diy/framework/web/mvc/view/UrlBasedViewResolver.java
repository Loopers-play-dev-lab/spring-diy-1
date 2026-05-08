package com.diy.framework.web.mvc.view;

public class UrlBasedViewResolver implements ViewResolver {
    private final String REDIRECT_PREFIX = "redirect:";

    @Override
    public View resolveViewName(String viewName) {
        if(viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }

        return null;
    }
}
