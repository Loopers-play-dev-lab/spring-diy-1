package com.diy.framework.web.servlet.config.annotation;

import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Configuration;
import com.diy.framework.web.http.converter.HttpMessageConverter;
import com.diy.framework.web.http.converter.json.MappingJackson2HttpMessageConverter;
import com.diy.framework.web.method.RequestMappingHandlerMapping;
import com.diy.framework.web.method.support.HandlerMethodArgumentResolver;
import com.diy.framework.web.method.support.HandlerMethodReturnValueHandler;
import com.diy.framework.web.mvc.SimpleControllerHandlerAdapter;
import com.diy.framework.web.mvc.method.RequestResponseBodyMethodProcessor;
import com.diy.framework.web.mvc.method.ServletRequestMethodArgumentResolver;
import com.diy.framework.web.mvc.method.ServletResponseMethodArgumentResolver;
import com.diy.framework.web.mvc.method.ViewNameMethodReturnValueHandler;
import com.diy.framework.web.mvc.method.annotation.RequestMappingHandlerAdapter;
import com.diy.framework.web.mvc.view.JspViewResolver;
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

    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers(final RequestResponseBodyMethodProcessor bodyProcessor) {
        final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        resolvers.add(bodyProcessor);
        return resolvers;
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers(final RequestResponseBodyMethodProcessor bodyProcessor) {
        final List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
        handlers.add(bodyProcessor);
        handlers.add(new ViewNameMethodReturnValueHandler());
        return handlers;
    }

    private List<HttpMessageConverter> getDefaultMessageConverters() {
        final List<HttpMessageConverter> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;
    }
}
