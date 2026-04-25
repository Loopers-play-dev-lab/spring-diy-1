package com.diy.framework.web.server.servlet.views;

public class HtmlViewResolver implements ViewResolver {
  private static String FILE_EXTENSION = ".html";

  @Override
  public View resolve(String viewName) {
    if (viewName == null || viewName.isBlank()) {
      return new HtmlView(viewName);
    }

    if (viewName.startsWith("redirect:")) {
      return new HtmlView(viewName.replace(View.REDIRECT_PREFIX, ""));
    }

    return new HtmlView(viewName + FILE_EXTENSION);
  }
}
