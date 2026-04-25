package com.diy.app.controller.api;

import com.diy.app.store.Lecture;
import com.diy.app.store.LectureStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final LectureStore lectureStore = new LectureStore();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    final String body = new String(req.getInputStream().readAllBytes());
    var createRequest = objectMapper.readValue(body, LectureRequest.Create.class);
    lectureStore.add(createRequest);

    resp.sendRedirect("/lecture-list");
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    final String body = new String(req.getInputStream().readAllBytes());
    var updateRequest = objectMapper.readValue(body, LectureRequest.Update.class);
    lectureStore.update(updateRequest.toLecture());

    resp.sendRedirect("/lecture-list");
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    final Long id = Long.valueOf(req.getParameter("id"));
    lectureStore.delete(id);

    resp.sendRedirect("/lecture-list");
  }

}
