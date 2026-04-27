package com.diy.framework.web.view.resolver;

import com.diy.framework.web.view.RedirectView;
import com.diy.framework.web.view.View;
import com.diy.framework.web.view.ViewResolver;

public class RedirectViewResolver implements ViewResolver {
    @Override
    public boolean match(String viewName) {
        return viewName.startsWith("redirect:");
    }

    @Override
    public View resolveViewName(String viewName) {
        return new RedirectView(viewName);
    }
}
