package com.diy.app.config;

import com.diy.framework.web.beans.annotations.Bean;
import com.diy.framework.web.beans.annotations.Configuration;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.mvc.view.ViewResolver;

@Configuration
public class AppConfig {

    @Bean
    public ViewResolver jspViewResolver() {
        return JspViewResolver.getInstance();
    }

    @Bean
    public ViewResolver urlBasedViewResolver() {
        return UrlBasedViewResolver.getInstance();
    }
}
