package com.diy.framework.web.mvc;

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

    @Bean
    public AnnotationHandlerMapping annotationHandlerMapping() {
        AnnotationHandlerMapping mapping = new AnnotationHandlerMapping();
        mapping.setOrder(0);
        return mapping;
    }

    @Bean
    public ControllerHandlerMapping controllerHandlerMapping() {
        ControllerHandlerMapping mapping = new ControllerHandlerMapping();
        mapping.setOrder(2);
        return mapping;
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
