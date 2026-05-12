package com.diy.app;

import com.diy.framework.web.beans.factory.annotation.Bean;
import com.diy.framework.web.beans.factory.annotation.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AppConfig {

    @Bean("ObjectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
