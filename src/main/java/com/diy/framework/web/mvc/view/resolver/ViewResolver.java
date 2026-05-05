package com.diy.framework.web.mvc.view.resolver;

import com.diy.framework.web.mvc.view.View;

@FunctionalInterface
public interface ViewResolver {
    View resolveViewName(String viewName);
}
