package com.diy.framework.web.mvc.view.resolver;

import com.diy.framework.web.mvc.view.JspView;
import com.diy.framework.web.mvc.view.View;

public class JspViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String viewName) {
        return new JspView(viewName + ".jsp");
    }
}
