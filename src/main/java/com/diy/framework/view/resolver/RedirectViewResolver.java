package com.diy.framework.view.resolver;

import com.diy.framework.view.RedirectView;
import com.diy.framework.view.View;
import com.diy.framework.view.ViewType;

public class RedirectViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        final String marker = ViewType.REDIRECT.getMarker();
        if (!viewName.startsWith(marker)) {
            return null;
        }
        return new RedirectView(viewName.substring(marker.length()));
    }
}
