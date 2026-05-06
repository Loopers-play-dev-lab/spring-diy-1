package com.diy.framework.web.mvc.view;

public class DefaultViewResolver implements ViewResolver {
    @Override
    public View resolve(final String viewName) {
        if (viewName.startsWith("redirect:")) {
            return new RedirectView(viewName.substring("redirect:".length()));
        }
        return new JspView("/" + viewName + ".jsp");
    }
}
