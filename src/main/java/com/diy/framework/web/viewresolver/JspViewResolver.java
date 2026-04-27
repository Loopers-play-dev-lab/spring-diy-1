package com.diy.framework.web.viewresolver;

import com.diy.framework.web.view.HtmlView;
import com.diy.framework.web.view.JspView;
import com.diy.framework.web.view.View;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.contains("redirect:")) {
            return new JspView(viewName);
        }

        String extensionName = viewName.split("\\.")[1];
        if (!extensionName.equals("jsp")) {
            throw new RuntimeException("500 Internal Server Error");
        }
        return new JspView(viewName);
    }
}
