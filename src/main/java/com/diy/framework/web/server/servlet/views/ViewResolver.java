package com.diy.framework.web.server.servlet.views;

@FunctionalInterface
public interface ViewResolver {
  View resolve(String viewName);
}
