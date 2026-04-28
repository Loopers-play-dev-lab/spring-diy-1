package com.diy.framework.web.mvc.view;

import java.util.List;

public class ViewResolverRegistry {
    private final List<ViewResolver> resolvers;

    public ViewResolverRegistry(List<ViewResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public List<ViewResolver> getResolvers() {
        return resolvers;
    }
}
