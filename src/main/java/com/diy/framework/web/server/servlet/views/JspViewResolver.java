package com.diy.framework.web.server.servlet.views;

public class JspViewResolver implements ViewResolver {
  private static String FILE_EXTENSION = ".jsp";

  @Override
  public View resolve(String viewName) {
    if (viewName == null || viewName.isBlank()) {
      return new JspView(viewName);
    }

    if (viewName.startsWith(View.REDIRECT_PREFIX)) {
      return new JspView(viewName);
    }

    return new JspView(viewName + FILE_EXTENSION);
  }
}
