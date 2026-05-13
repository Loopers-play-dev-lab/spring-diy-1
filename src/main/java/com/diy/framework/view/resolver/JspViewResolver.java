package com.diy.framework.view.resolver;

import com.diy.framework.view.JspView;
import com.diy.framework.view.View;
import com.diy.framework.view.ViewType;

public class JspViewResolver implements ViewResolver {
    private static final String VIEW_PREFIX = "/";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(ViewType.REDIRECT.getMarker())) {
            return null;
        }
        return new JspView(VIEW_PREFIX + viewName + ViewType.JSP.getMarker());
    }
}
