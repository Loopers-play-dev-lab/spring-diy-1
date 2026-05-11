package com.diy.framework.view.resolver;

import com.diy.framework.view.MustacheView;
import com.diy.framework.view.View;
import com.diy.framework.view.ViewType;

public class MustacheViewResolver implements ViewResolver {

    private static final String VIEW_PREFIX = "/";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(ViewType.REDIRECT.getMarker())) {
            return null;
        }
        return new MustacheView(VIEW_PREFIX + viewName + ViewType.MUSTACHE.getMarker());
    }
}
