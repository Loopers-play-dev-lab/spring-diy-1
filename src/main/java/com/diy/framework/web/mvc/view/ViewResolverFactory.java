package com.diy.framework.web.mvc.view;

public class ViewResolverFactory {
    private static final ViewResolver JSP_VIEW_RESOLVER = new JspViewResolver();
    private static final ViewResolver URL_BASED_VIEW_RESOLVER = new UrlBasedViewResolver();

    public static ViewResolver getViewResolver(String viewName) {
        if(viewName.startsWith("redirect:")) {
            return URL_BASED_VIEW_RESOLVER;
        } else {
            return JSP_VIEW_RESOLVER;
        }
    }
}
