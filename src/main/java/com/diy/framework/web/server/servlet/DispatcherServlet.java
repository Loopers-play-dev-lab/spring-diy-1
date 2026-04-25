package com.diy.framework.web.server.servlet;

import com.diy.framework.web.server.exceptions.CustomException;
import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.controller.ControllerResolver;
import com.diy.framework.web.server.servlet.utils.HttpServletUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
  private ControllerResolver controllerResolver;

  @Override
  public void init() {
    controllerResolver = new ControllerResolver();
  }

  @Override
  protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    String path = HttpServletUtils.getUriPath(req);
    req.setAttribute("params", HttpServletUtils.parseBody(req));

    try {
      Controller controller = controllerResolver.resolve(path);
      controller.handleRequest(req, resp);
    } catch (CustomException e) {
      resp.sendError(e.getHttpCode(), e.getMessage());
    }
  }

}
