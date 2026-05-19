package com.diy.framework.web.servlet;

import com.diy.framework.web.mvc.view.HtmlViewResolver;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;
import java.util.List;
import java.util.Objects;

public class ViewResolverComposite {

    private final List<ViewResolver> viewResolvers;

    public ViewResolverComposite() {
        this.viewResolvers = List.of(
                new UrlBasedViewResolver(),
                new JspViewResolver(),
                new HtmlViewResolver()
        );
    }

    public View resolve(String viewName) {
        return viewResolvers.stream()
                .map(resolver -> resolver.resolveViewName(viewName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View not found: " + viewName));
    }
}
