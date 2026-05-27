package com.diy.framework.context.fixture.bean;

import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;

@Component
public class AppConfig {

    @Bean(value = "dataSource")
    public DataSource dataSource() {
        return new DataSource();
    }
}
