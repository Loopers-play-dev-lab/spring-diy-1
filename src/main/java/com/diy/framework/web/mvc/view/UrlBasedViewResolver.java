package com.diy.framework.web.mvc.view;

public class UrlBasedViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(final String viewName) {
        if (!viewName.startsWith("redirect:")) {
            return null;
        }

        final String redirectUrl = viewName.substring("redirect:".length());
        return new RedirectView(redirectUrl);
    }
}
