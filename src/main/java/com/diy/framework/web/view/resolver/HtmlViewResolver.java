package com.diy.framework.web.view.resolver;

import com.diy.framework.web.view.HtmlView;
import com.diy.framework.web.view.View;
import com.diy.framework.web.view.ViewResolver;

public class HtmlViewResolver implements ViewResolver {

    @Override
    public boolean match(String viewName) {
        return viewName.endsWith("html");
    }

    @Override
    public View resolveViewName(String viewName) {
        return new HtmlView(viewName);
    }
}
