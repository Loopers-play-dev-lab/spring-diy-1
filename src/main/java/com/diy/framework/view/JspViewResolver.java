package com.diy.framework.view;

public class JspViewResolver implements ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String PREFIX = "/";
    private static final String SUFFIX = ".jsp";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }

        String fullPath = PREFIX + viewName + SUFFIX;
        return new JspView(fullPath);
    }
}
