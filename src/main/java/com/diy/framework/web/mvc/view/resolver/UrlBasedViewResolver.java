package com.diy.framework.web.mvc.view.resolver;

import com.diy.framework.web.mvc.view.RedirectView;
import com.diy.framework.web.mvc.view.View;

public class UrlBasedViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String viewName) {
        if (!viewName.startsWith("redirect:")) {
            return null;
        }

        final String redirectUrl = viewName.substring("redirect:".length());
        return new RedirectView(redirectUrl);
    }
}
