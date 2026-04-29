package com.diy.framework.web.config;

import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.mvc.view.ViewResolver;

public class WebConfig {

    public static ViewResolver jspViewResolver() {
        return JspViewResolver.getInstance();
    }

    public static ViewResolver urlBasedViewResolver() {
        return UrlBasedViewResolver.getInstance();
    }
}
