package com.diy.framework.web.server;

public interface ViewResolver {
    View resolve(final String viewName);
}
