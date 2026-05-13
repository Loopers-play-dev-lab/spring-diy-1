package com.diy.framework.web.beans.factory.metadata;

import com.diy.framework.web.beans.annotations.Bean;
import java.lang.reflect.Method;

public record MethodBeanMetadata(
  String name,
  Method method
) implements BeanMetadata {

  @Override
  public Class<?> target() {
    return method.getReturnType();
  }

  public static MethodBeanMetadata from(Method method) {
    String value = method.getAnnotation(Bean.class).value();
    return new MethodBeanMetadata(
        value.isBlank() ? method.getReturnType().getSimpleName() : value,
        method
    );
  }

}
