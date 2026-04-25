package com.diy.app.controller.api;

import com.diy.app.store.LectureStore;
import com.diy.framework.web.server.exceptions.MethodNotAllowedException;
import com.diy.framework.web.server.interfaces.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureController implements Controller {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final LectureStore lectureStore = new LectureStore();

  @Override
  public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    switch (request.getMethod()) {
      case "POST" -> create(request, response);
      case "PUT" -> update(request, response);
      case "DELETE" -> delete(request, response);
      default -> throw new MethodNotAllowedException();
    }
  }

  public void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    var createRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Create.class);
    lectureStore.add(createRequest);

    resp.sendRedirect("/lecture-list");
  }

  public void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    var updateRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Update.class);
    lectureStore.update(updateRequest.toLecture());

    resp.sendRedirect("/lecture-list");
  }

  public void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    var deleteRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Delete.class);
    lectureStore.delete(deleteRequest.id());

    resp.sendRedirect("/lecture-list");
  }
}
