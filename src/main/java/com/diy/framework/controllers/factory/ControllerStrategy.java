package com.diy.framework.controllers.factory;

public interface ControllerStrategy {
  boolean supports(Class<?> beanClass);
  void register(Class<?> clazz, Object bean, ControllerRegistry registry);
}
