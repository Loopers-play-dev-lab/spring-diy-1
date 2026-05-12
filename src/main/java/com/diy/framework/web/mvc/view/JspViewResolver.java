package com.diy.framework.web.mvc.view;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        String fullPath = String.format("/%s.jsp", viewName);
        return new JspView(fullPath);
    }
}
