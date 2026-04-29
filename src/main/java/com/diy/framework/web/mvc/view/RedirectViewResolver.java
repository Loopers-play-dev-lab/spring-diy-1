package com.diy.framework.web.mvc.view;

public class RedirectViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (!viewName.startsWith(RedirectView.PREFIX)) {
            return null;
        }
        final String location = viewName.substring(RedirectView.PREFIX.length());
        return new RedirectView(location);
    }
}
