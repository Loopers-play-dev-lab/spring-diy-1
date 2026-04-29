package com.diy.framework.web.mvc.view;

public class JspViewResolver implements ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public View resolveViewName(final String viewName) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }
        return new JspView(viewName + ".jsp");
    }
}
