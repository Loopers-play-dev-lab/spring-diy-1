package com.diy.app.config;

import com.diy.app.annotation.Bean;
import com.diy.app.annotation.Component;
import com.diy.app.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public String defaultTest() {
        return "defaultTest 생성됨";
    }
    @Bean(value = "valueTest")
    public String valueTest() {
        return "valueTest 생성됨";
    }
    @Bean(name = "nameTest")
    public String nameTest() {
        return "nameTest 생성됨";
    }
}
