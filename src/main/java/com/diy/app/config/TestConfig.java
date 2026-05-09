package com.diy.app.config;

import com.diy.app.domain.Car;
import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;

@Component
public class TestConfig {

    @Bean("car")
    public Car car() {
        return new Car();
    }
}
