package com.diy.framework.web.server.mv;

public class JspViewResolver implements ViewResolver {
    @Override
    public View resolve(final String viewName) {
        if (viewName == null || viewName.isBlank()) {
            return null;
        }

        if (viewName.startsWith("redirect:")) {
            return new RedirectView(viewName.substring("redirect:".length()));
        }
        return new JspView("/" + viewName + ".jsp");
    }
}
