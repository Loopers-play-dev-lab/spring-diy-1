package com.diy.framework.controllers.factory;

import com.diy.framework.context.RequestMethod;
import com.diy.framework.web.mvc.IController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class ControllerRegistry {

  private final List<ControllerStrategy> strategies = List.of(
      new AnnotationControllerStrategy(),
      new InterfaceControllerStrategy()
  );
  private final Map<Key, IController> mapping = new HashMap<>();

  public void register(Class<?> clazz, Object bean) {
    List<ControllerStrategy> matched = strategies.stream()
        .filter(strategy -> strategy.supports(clazz))
        .toList();

    if (matched.size() > 1) {
      throw new RuntimeException("컨트롤러 등록 방식 충돌: " + clazz.getName());
    }
    matched.forEach(strategy -> strategy.register(clazz, bean, this));
  }

  public void add(RequestMethod method, String url, IController controller) {
    mapping.put(new Key(method, url), controller);
  }

  public IController get(HttpServletRequest req) {
    return mapping.get(new Key(RequestMethod.valueOf(req.getMethod()), req.getRequestURI()));
  }

  public record Key(
      RequestMethod method,
      String url
  ) {}

}
