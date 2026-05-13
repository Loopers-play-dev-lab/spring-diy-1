package com.diy.app.config;

import com.diy.framework.web.beans.annotations.Bean;
import com.diy.framework.web.beans.annotations.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ObjectMapperConfig {

  @Bean("objectMapper")
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

}
