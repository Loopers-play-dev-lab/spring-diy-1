package com.diy.framework.web.server.mv;

public class UrlBasedViewResolver implements ViewResolver {
    @Override
    public View resolve(final String viewName) {
        if (!viewName.startsWith("/")) {
            return null;
        }
        final String redirectUrl = viewName.substring("redirect:".length());
        return new RedirectView(redirectUrl);
    }
}
