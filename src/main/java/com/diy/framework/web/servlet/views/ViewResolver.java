package com.diy.framework.web.servlet.views;

@FunctionalInterface
public interface ViewResolver {
  View resolveViewName(String viewName);
}
