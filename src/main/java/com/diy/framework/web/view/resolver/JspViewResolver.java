package com.diy.framework.web.view.resolver;

import com.diy.framework.web.view.JspView;
import com.diy.framework.web.view.View;
import com.diy.framework.web.view.ViewResolver;

public class JspViewResolver implements ViewResolver {

    @Override
    public boolean match(String viewName) {
        return viewName.endsWith("jsp");
    }

    @Override
    public View resolveViewName(String viewName) {
        return new JspView(viewName);
    }
}
