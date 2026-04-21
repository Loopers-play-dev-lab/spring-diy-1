package com.diy.app.controller.api;

import com.diy.app.store.LectureStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lectures/*")
public class LectureServlet extends HttpServlet {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final LectureStore lectureStore = new LectureStore();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    var createRequest = objectMapper.readValue(req.getInputStream(), LectureRequest.Create.class);
    lectureStore.add(createRequest);

    redirect(resp);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Long id = getId(req);
    var updateRequest = objectMapper.readValue(req.getInputStream(), LectureRequest.Update.class);
    lectureStore.update(updateRequest.toLecture(id));

    redirect(resp);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Long id = getId(req);
    lectureStore.delete(id);

    redirect(resp);
  }

  private Long getId(HttpServletRequest req) {
    String path = req.getPathInfo();
    if (path == null || path.length() <= 1) throw new IllegalArgumentException("잘못된 ID입니다.");

    try {
      return Long.parseLong(path.substring(1));
    } catch (NumberFormatException _) {
      throw new IllegalArgumentException("유효한 ID값을 입력해야 합니다.");
    }
  }

  private void redirect(HttpServletResponse resp) throws IOException {
      resp.setStatus(HttpServletResponse.SC_FOUND);
      resp.sendRedirect("/lecture-list");
  }

}
