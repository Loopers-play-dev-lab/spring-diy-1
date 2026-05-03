package com.diy.framework.web.view;

public interface ViewResolver {
    boolean match(String viewName);
    View resolveViewName(String viewName);
}
