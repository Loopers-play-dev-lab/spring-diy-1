package com.diy.framework.view;

import java.io.IOException;

public class RedirectViewResolver implements ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public View resolveViewName(String viewName) throws IOException {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }

        return null;
    }
}
