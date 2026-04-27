package com.diy.framework.web.viewresolver;

import com.diy.framework.web.view.JspView;
import com.diy.framework.web.view.View;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.contains("redirect:")) {
            String path = viewName.substring(viewName.indexOf(":") + 1);
            return new JspView(path, true);
        }
        if (!viewName.contains("jsp")) {
            return new JspView(viewName + ".jsp");
        } else {
            return new JspView(viewName);
        }
    }
}
