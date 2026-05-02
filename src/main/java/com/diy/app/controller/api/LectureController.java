package com.diy.app.controller.api;

import com.diy.app.store.LectureStore;
import com.diy.framework.web.server.exceptions.MethodNotAllowedException;
import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.servlet.views.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureController implements Controller {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final LectureStore lectureStore = new LectureStore();

  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    return switch (request.getMethod()) {
      case "POST" -> create(request);
      case "PUT" -> update(request);
      case "DELETE" -> delete(request);
      default -> throw new MethodNotAllowedException();
    };
  }

  public ModelAndView create(HttpServletRequest req) {
    var createRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Create.class);
    lectureStore.add(createRequest);

    return new ModelAndView("redirect:lecture-list");
  }

  public ModelAndView update(HttpServletRequest req) {
    var updateRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Update.class);
    lectureStore.update(updateRequest.toLecture());

    return new ModelAndView("redirect:lecture-list");
  }

  public ModelAndView delete(HttpServletRequest req) {
    var deleteRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Delete.class);
    lectureStore.delete(deleteRequest.id());

    return new ModelAndView("redirect:lecture-list");
  }
}
