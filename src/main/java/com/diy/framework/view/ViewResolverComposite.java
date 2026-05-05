package com.diy.framework.view;

import java.util.List;
import java.util.Objects;

public class ViewResolverComposite {

    private final List<ViewResolver> viewResolvers;

    public ViewResolverComposite(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public View resolve(String viewName) {
        return viewResolvers.stream()
                .map(resolver -> resolver.resolveViewName(viewName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View not found: " + viewName));
    }
}
