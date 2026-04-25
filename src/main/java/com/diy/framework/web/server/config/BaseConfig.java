package com.diy.framework.web.server.config;

import com.diy.framework.web.server.servlet.views.JspViewResolver;
import com.diy.framework.web.server.servlet.views.ViewResolver;

public class BaseConfig {

  public static ViewResolver viewResolver() {
    return new JspViewResolver();
  }
}
