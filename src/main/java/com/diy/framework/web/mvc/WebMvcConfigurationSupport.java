package com.diy.framework.web.mvc;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Configuration;
import com.diy.framework.web.mvc.view.HtmlViewResolver;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.servlet.handler.AnnotationHandlerMapping;
import com.diy.framework.web.servlet.handler.ControllerHandlerMapping;
import com.diy.framework.web.servlet.handler.adapter.AnnotationHandlerAdapter;
import com.diy.framework.web.servlet.handler.adapter.ControllerHandlerAdapter;

@Configuration
public class WebMvcConfigurationSupport {

    private final ApplicationContext context;

    public WebMvcConfigurationSupport(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public AnnotationHandlerMapping annotationHandlerMapping() {
        return new AnnotationHandlerMapping(context);
    }

    @Bean
    public ControllerHandlerMapping controllerHandlerMapping() {
        return new ControllerHandlerMapping(context);
    }

    @Bean
    public AnnotationHandlerAdapter annotationHandlerAdapter() {
        return new AnnotationHandlerAdapter();
    }

    @Bean
    public ControllerHandlerAdapter controllerHandlerAdapter() {
        return new ControllerHandlerAdapter();
    }

    @Bean
    public UrlBasedViewResolver urlBasedViewResolver() {
        return new UrlBasedViewResolver();
    }

    @Bean
    public JspViewResolver jspViewResolver() {
        return new JspViewResolver();
    }

    @Bean
    public HtmlViewResolver htmlViewResolver() {
        return new HtmlViewResolver();
    }
}
