package com.diy.framework.web.server.servlet;

import com.diy.framework.web.server.config.BaseConfig;
import com.diy.framework.web.server.exceptions.CustomException;
import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.controller.ControllerResolver;
import com.diy.framework.web.server.exceptions.NotFoundViewException;
import com.diy.framework.web.server.servlet.utils.HttpServletUtils;
import com.diy.framework.web.server.servlet.views.ModelAndView;
import com.diy.framework.web.server.servlet.views.View;
import com.diy.framework.web.server.servlet.views.ViewResolver;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
  private ControllerResolver controllerResolver;
  private ViewResolver viewResolver;

  @Override
  public void init() {
    controllerResolver = new ControllerResolver();
    viewResolver = BaseConfig.viewResolver();
  }

  @Override
  protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
    String path = HttpServletUtils.getUriPath(req);
    req.setAttribute("params", HttpServletUtils.parseBody(req));

    try {
      Controller controller = controllerResolver.resolve(path);
      ModelAndView mav = controller.handleRequest(req, resp);
      render(mav, req, resp);
    } catch (CustomException e) {
      resp.sendError(e.getHttpCode(), e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void render(
      final ModelAndView mav,
      final HttpServletRequest req,
      final HttpServletResponse resp
  ) throws ServletException, IOException {
    final String viewName = mav.getViewName();

    final View view = viewResolver.resolveViewName(viewName);

    if (view == null) {
      throw new NotFoundViewException(viewName);
    }

    view.render(mav.getModel(), req, resp);
  }

}
