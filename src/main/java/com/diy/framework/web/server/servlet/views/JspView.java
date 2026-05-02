package com.diy.framework.web.server.servlet.views;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {
  private final String viewName;

  public JspView(final String viewName) {
    this.viewName = viewName;
  }

  public void render(
      Map<String, Object> model,
      final HttpServletRequest req,
      final HttpServletResponse res
  ) throws ServletException, IOException {
    if (viewName == null || viewName.isBlank()) return;

    if (viewName.startsWith(REDIRECT_PREFIX)) {
      res.sendRedirect(viewName.replace(REDIRECT_PREFIX, ""));
      return;
    }

    model.forEach(req::setAttribute);
    req.getRequestDispatcher(viewName).forward(req, res);
  }
}
