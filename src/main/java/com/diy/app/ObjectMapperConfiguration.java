package com.diy.app;

import com.diy.framework.bean.Bean;
import com.diy.framework.bean.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        System.out.println("Bean initialize!!");
        return new ObjectMapper();
    }
}
