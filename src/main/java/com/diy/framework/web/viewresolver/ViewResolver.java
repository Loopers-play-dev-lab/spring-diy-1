package com.diy.framework.web.viewresolver;

import com.diy.framework.web.view.View;

public interface ViewResolver {
    View resolveViewName(String viewName);
}
