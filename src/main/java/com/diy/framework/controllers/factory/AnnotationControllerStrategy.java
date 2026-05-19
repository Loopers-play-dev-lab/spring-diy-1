package com.diy.framework.controllers.factory;

import com.diy.framework.context.RequestMethod;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.context.annotation.RequestMapping;
import com.diy.framework.web.mvc.IController;
import com.diy.framework.web.mvc.view.ModelAndView;
import java.lang.reflect.Method;

public class AnnotationControllerStrategy implements ControllerStrategy {

  @Override
  public boolean supports(Class<?> beanClass) {
    return beanClass.isAnnotationPresent(Controller.class);
  }

  @Override
  public void register(Class<?> clazz, Object bean, ControllerRegistry registry) {
    RequestMapping requestMapping = clazz.getDeclaredAnnotation(RequestMapping.class);
    String baseUrl = (requestMapping == null) ? "" : requestMapping.value();

    for (Method method : clazz.getMethods()) {
      RequestMapping mapping = method.getAnnotation(RequestMapping.class);
      if (mapping == null) continue;

      String url = baseUrl + "/" + mapping.value().replaceFirst("/", "");
      IController controller = (req, resp) -> (ModelAndView) method.invoke(bean, req, resp);

      for (RequestMethod httpMethod : mapping.methods()) {
        registry.add(httpMethod, url, controller);
      }
    }
  }

}
