package com.diy.app.controller.view;

import com.diy.app.store.Lecture;
import com.diy.app.store.LectureStore;
import com.diy.framework.web.server.config.BaseConfig;
import com.diy.framework.web.server.exceptions.MethodNotAllowedException;
import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.servlet.views.View;
import com.diy.framework.web.server.servlet.views.ViewResolver;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureListController implements Controller {

  private final LectureStore lectureStore = new LectureStore();
  private final ViewResolver viewResolver = BaseConfig.viewResolver();

  @Override
  public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    switch (request.getMethod()) {
      case "GET" -> findAll(request, response);
      default -> throw new MethodNotAllowedException();
    }
  }

  public void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     List<Lecture> lectures = lectureStore.list();

     req.setAttribute("lectures", lectures);
     View view = viewResolver.resolve("lecture-list");
     view.render(req, resp);
  }
}
