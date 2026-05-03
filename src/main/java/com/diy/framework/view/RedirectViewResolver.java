package com.diy.framework.view;

public class RedirectViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(final String viewName) {
        if(!viewName.startsWith("redirect:")){
            return null;
        }

        final String redirectUrl = viewName.substring("redirect:".length());
        return new RedirectView(redirectUrl);
    }
}
