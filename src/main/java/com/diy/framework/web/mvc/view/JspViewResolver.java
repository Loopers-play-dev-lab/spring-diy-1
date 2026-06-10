package com.diy.framework.web.mvc.view;

import com.diy.framework.core.Ordered;

public class JspViewResolver implements ViewResolver, Ordered {

    @Override
    public View resolveViewName(final String viewName) {
        String jspViewName = viewName + ".jsp";
        java.net.URL url = this.getClass().getClassLoader().getResource(jspViewName);
        if (url == null) {return null;}
        return new JspView("/" + viewName + ".jsp");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
