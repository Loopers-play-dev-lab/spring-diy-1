package com.diy.framework.web.mvc.view.resolver;

import com.diy.framework.web.mvc.view.HtmlView;
import com.diy.framework.web.mvc.view.View;

public class HtmlViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String viewName) {
        return new HtmlView("/templates/" + viewName + ".html");
    }
}
