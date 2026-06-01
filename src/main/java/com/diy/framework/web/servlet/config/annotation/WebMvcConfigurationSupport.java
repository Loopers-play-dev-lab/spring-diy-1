package com.diy.framework.web.servlet.config.annotation;

import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Configuration;
import com.diy.framework.web.method.RequestMappingHandlerMapping;
import com.diy.framework.web.mvc.SimpleControllerHandlerAdapter;
import com.diy.framework.web.mvc.method.RequestMappingHandlerAdapter;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.servlet.handler.BeanNameUrlHandlerMapping;

@Configuration
public class WebMvcConfigurationSupport {

    @Bean
   	public BeanNameUrlHandlerMapping beanNameHandlerMapping() {
        final BeanNameUrlHandlerMapping mapping = new BeanNameUrlHandlerMapping();
        mapping.setOrder(2);

        return mapping;
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        final RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
        mapping.setOrder(1);

        return mapping;
    }

    @Bean
   	public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter() {
   		return new SimpleControllerHandlerAdapter();
   	}

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }

    @Bean
    public JspViewResolver jspViewResolver() {
        return new JspViewResolver();
    }
}
