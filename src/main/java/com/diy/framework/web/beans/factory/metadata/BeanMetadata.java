package com.diy.framework.web.beans.factory.metadata;

public sealed interface BeanMetadata permits ComponentBeanMetadata, MethodBeanMetadata {

  String name();
  Class<?> target();

}
