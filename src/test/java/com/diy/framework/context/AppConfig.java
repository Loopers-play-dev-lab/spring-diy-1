package com.diy.framework.context;

import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;

@Component
public class AppConfig {

    @Bean("methodBean")
    public Foo methodBean() {
        return new Foo("Bean");
    }
}
