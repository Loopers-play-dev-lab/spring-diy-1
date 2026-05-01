package com.diy.framework.view;

public class UrlBasedViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(final String viewName) {
        if (!viewName.startsWith("redirect:")) {
            return null;
        }

        String redirectUrl = viewName.substring("redirect:".length());
        return new RedirectView(redirectUrl);
    }
}