package com.diy.framework.web.config;

import com.diy.framework.web.view.JspViewResolver;
import com.diy.framework.web.view.ViewResolver;

public class WebConfig {

    public static ViewResolver viewResolver() {
        return JspViewResolver.getInstance();
    }
}
