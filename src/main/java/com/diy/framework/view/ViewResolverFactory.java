package com.diy.framework.view;

import com.diy.framework.view.resolver.JspViewResolver;
import com.diy.framework.view.resolver.RedirectViewResolver;
import com.diy.framework.view.resolver.ViewResolver;

public final class ViewResolverFactory {
    private ViewResolverFactory() {}

    public static ViewResolver of(ViewType viewType) {
        return switch (viewType) {
            case JSP -> new JspViewResolver();
            case REDIRECT -> new RedirectViewResolver();
            case MUSTACHE -> throw new RuntimeException("Unsupported view type: " + viewType);
        };
    }
}
