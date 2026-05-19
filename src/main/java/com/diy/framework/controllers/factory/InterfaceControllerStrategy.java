package com.diy.framework.controllers.factory;

import com.diy.framework.context.RequestMethod;
import com.diy.framework.context.annotation.RequestMapping;
import com.diy.framework.web.mvc.IController;

public class InterfaceControllerStrategy implements ControllerStrategy {

  @Override
  public boolean supports(Class<?> beanClass) {
    return IController.class.isAssignableFrom(beanClass)
        && beanClass.isAnnotationPresent(RequestMapping.class);
  }

  @Override
  public void register(Class<?> clazz, Object bean, ControllerRegistry registry) {
    RequestMapping requestMapping = clazz.getDeclaredAnnotation(RequestMapping.class);
    IController controller = (IController) bean;

    for (RequestMethod method : requestMapping.methods()) {
      registry.add(method, requestMapping.value(), controller);
    }
  }

}
