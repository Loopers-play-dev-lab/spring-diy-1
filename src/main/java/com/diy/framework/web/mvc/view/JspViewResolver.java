package com.diy.framework.web.mvc.view;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(final String viewName) {
        if (viewName.startsWith("redirect:")) {
            return new RedirectView(viewName.substring("redirect:".length()));
        }
        return new JspView(viewName + ".jsp");
    }
}
