package com.diy.framework.web.mvc.view;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(final String viewName) {
        String jspViewName = viewName + ".jsp";
        java.net.URL url = this.getClass().getClassLoader().getResource(jspViewName);
        if (url == null) {return null;}
        return new JspView("/" + viewName + ".jsp");
    }
}
