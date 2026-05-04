package com.diy.framework.web.view;

public class JspViewResolver {

    public View resolveViewName(final String viewName) {
        if (viewName.startsWith("redirect:")) {
            return new RedirectView(viewName.substring("redirect:".length()));
        }
        return new JspView("/" + viewName + ".jsp");
    }
}
