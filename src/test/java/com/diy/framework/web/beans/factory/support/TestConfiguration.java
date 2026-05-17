package com.diy.framework.web.beans.factory.support;

import com.diy.framework.web.annotations.Bean;
import com.diy.framework.web.annotations.Configuration;

@Configuration
class TestConfiguration {

    @Bean
    TestBeanClient testBeanClient(NamedRepository namedRepository) {
        return new TestBeanClient(namedRepository);
    }
}
