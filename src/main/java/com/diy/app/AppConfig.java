package com.diy.app;

import com.diy.framework.web.beans.factory.Bean;
import com.diy.framework.web.beans.factory.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AppConfig {

    @Bean("objectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
