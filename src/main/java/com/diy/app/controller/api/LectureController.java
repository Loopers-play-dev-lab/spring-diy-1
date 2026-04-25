package com.diy.app.controller.api;

import com.diy.app.store.LectureStore;
import com.diy.framework.web.server.config.BaseConfig;
import com.diy.framework.web.server.exceptions.MethodNotAllowedException;
import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.servlet.views.View;
import com.diy.framework.web.server.servlet.views.ViewResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureController implements Controller {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final LectureStore lectureStore = new LectureStore();
  private final ViewResolver viewResolver = BaseConfig.viewResolver();

  @Override
  public void handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    switch (request.getMethod()) {
      case "POST" -> create(request, response);
      case "PUT" -> update(request, response);
      case "DELETE" -> delete(request, response);
      default -> throw new MethodNotAllowedException();
    }
  }

  public void create(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    var createRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Create.class);
    lectureStore.add(createRequest);

    View view = viewResolver.resolve("redirect:lecture-list");
    view.render(req, resp);
  }

  public void update(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    var updateRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Update.class);
    lectureStore.update(updateRequest.toLecture());

    View view = viewResolver.resolve("redirect:lecture-list");
    view.render(req, resp);
  }

  public void delete(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    var deleteRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Delete.class);
    lectureStore.delete(deleteRequest.id());

    View view = viewResolver.resolve("redirect:lecture-list");
    view.render(req, resp);
  }
}
