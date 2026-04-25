package com.diy.app.controller.api;

import com.diy.app.store.LectureStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureServlet extends HttpServlet {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final LectureStore lectureStore = new LectureStore();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    var createRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Create.class);
    lectureStore.add(createRequest);

    resp.sendRedirect("/lecture-list");
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    var updateRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Update.class);
    lectureStore.update(updateRequest.toLecture());

    resp.sendRedirect("/lecture-list");
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    var deleteRequest = objectMapper.convertValue(req.getAttribute("params"), LectureRequest.Delete.class);
    lectureStore.delete(deleteRequest.id());

    resp.sendRedirect("/lecture-list");
  }

}
