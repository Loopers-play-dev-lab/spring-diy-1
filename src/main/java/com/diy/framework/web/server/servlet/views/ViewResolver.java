package com.diy.framework.web.server.servlet.views;

@FunctionalInterface
public interface ViewResolver {
  View resolveViewName(String viewName);
}
