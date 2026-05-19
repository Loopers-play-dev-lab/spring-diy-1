package com.diy.framework.web.mvc.view;

import java.util.List;
import java.util.Objects;

public class ViewResolverComposite implements ViewResolver {

    private final List<ViewResolver> viewResolvers;

    public ViewResolverComposite() {
        this.viewResolvers = List.of(
                new UrlBasedViewResolver(),
                new JspViewResolver(),
                new HtmlViewResolver()
        );
    }

    @Override
    public View resolveViewName(String viewName) {
        return viewResolvers.stream()
                .map(resolver -> resolver.resolveViewName(viewName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View not found: " + viewName));
    }
}
