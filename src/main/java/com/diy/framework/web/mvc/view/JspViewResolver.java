package com.diy.framework.web.mvc.view;

public class JspViewResolver implements ViewResolver {

    private static final String PREFIX = "/";
    private static final String SUFFIX = ".jsp";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(RedirectView.PREFIX)) {
            return null;
        }
        return new JspView(PREFIX + viewName + SUFFIX);
    }
}
