package com.diy.framework.web.beans.factory.conflict;

import com.diy.framework.web.annotations.Bean;
import com.diy.framework.web.annotations.Component;
import com.diy.framework.web.annotations.Configuration;

@Component("duplicatedBean")
class DuplicateBeanA {
}

@Component("duplicatedBean")
class DuplicateBeanB {
}

@Configuration
class DuplicateBeanConfiguration {

    @Bean("duplicatedBean")
    String duplicateBeanFromFactory() {
        return "duplicate";
    }
}
