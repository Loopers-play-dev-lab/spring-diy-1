package com.diy.framework.web.servlet.config.annotation;

import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Configuration;
import com.diy.framework.web.http.converter.HttpMessageConverter;
import com.diy.framework.web.http.converter.json.MappingJackson2HttpMessageConverter;
import com.diy.framework.web.method.RequestMappingHandlerMapping;
import com.diy.framework.web.method.support.HandlerMethodArgumentResolver;
import com.diy.framework.web.method.support.HandlerMethodReturnValueHandler;
import com.diy.framework.web.mvc.SimpleControllerHandlerAdapter;
import com.diy.framework.web.mvc.method.RequestMappingHandlerAdapter;
import com.diy.framework.web.mvc.view.HttpMessageConverter;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.util.ArrayList;
import java.util.List;

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
        final RequestResponseBodyMethodProcessor bodyProcessor =
                new RequestResponseBodyMethodProcessor(getDefaultMessageConverters());
        return new RequestMappingHandlerAdapter(
                getDefaultArgumentResolvers(bodyProcessor),
                getDefaultReturnValueHandlers(bodyProcessor));
    }

    @Bean
    public JspViewResolver jspViewResolver() {
        return new JspViewResolver();
    }

    @Bean
    public UrlBasedViewResolver urlBasedViewResolver() { return new UrlBasedViewResolver(); }

    @Bean
    public HttpMessageConverter httpMessageConverter() { return new HttpMessageConverter(); }
}
