package com.diy.framework.web.server.interfaces;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {
  void handleRequest(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException;
}
