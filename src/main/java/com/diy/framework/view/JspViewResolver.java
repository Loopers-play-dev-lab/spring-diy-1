package com.diy.framework.view;

import javax.servlet.ServletContext;
import java.io.IOException;

public class JspViewResolver implements ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String SUFFIX = ".jsp";

    private final ServletContext servletContext;

    public JspViewResolver(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public View resolveViewName(String viewName) throws IOException {
        if(viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }

        String fullPath = viewName + SUFFIX;
        if (servletContext.getResource(fullPath) == null) {
            return null;
        }

        return new JspView(fullPath);
    }
}
