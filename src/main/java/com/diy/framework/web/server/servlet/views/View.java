package com.diy.framework.web.server.servlet.views;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface View {
  String REDIRECT_PREFIX = "redirect:";
  void render(final Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException;
}
