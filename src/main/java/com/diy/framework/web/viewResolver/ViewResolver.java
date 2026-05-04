package com.diy.framework.web.viewResolver;

import com.diy.framework.web.view.View;

public interface ViewResolver {
    View resolveViewName(String viewName);
}
