package com.diy.framework.web.beans.factory.metadata;

import java.lang.reflect.Constructor;

public record ComponentBeanMetadata(
    String name,
    Constructor<?> constructor
) implements BeanMetadata {

  @Override
  public Class<?> target() {
    return constructor.getDeclaringClass();
  }
}
