package com.diy.framework.view;

import javax.servlet.ServletContext;
import java.io.IOException;

public class JspViewResolver implements ViewResolver {

    private static final String SUFFIX = ".jsp";

    private final ServletContext servletContext;

    public JspViewResolver(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public View resolveViewName(String viewName) throws IOException {
        String fullPath = viewName + SUFFIX;
        if (servletContext.getResource(fullPath) == null) {
            return null;
        }

        return new JspView(fullPath);
    }
}
