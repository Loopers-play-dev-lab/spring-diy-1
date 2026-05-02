package com.diy.framework.web.server.controller;

import com.diy.framework.web.server.servlet.views.ModelAndView;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller {
  ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException;
}
