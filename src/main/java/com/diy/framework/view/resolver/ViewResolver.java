package com.diy.framework.view.resolver;

import com.diy.framework.view.View;

public interface ViewResolver {

    View resolveViewName(String viewName);
}
