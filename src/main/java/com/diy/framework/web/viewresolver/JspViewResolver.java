package com.diy.framework.web.viewresolver;

import com.diy.framework.web.view.JspView;
import com.diy.framework.web.view.View;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.contains("redirect:")) {
            return new JspView(viewName);
        }
        if (!viewName.contains("jsp")) {
            throw new RuntimeException("500 Internal Server Error");
        }
        return new JspView(viewName);
    }
}
