package com.diy.framework.web.mvc.view;

public class HttpMessageConverter implements ViewResolver {
    @Override
    public View resolveViewName(String viewName) {
        return new ObjectView(viewName);
    }
}
