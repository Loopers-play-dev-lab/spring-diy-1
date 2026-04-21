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

  private void redirect(HttpServletResponse resp) throws IOException {
      resp.setStatus(HttpServletResponse.SC_FOUND);
      resp.sendRedirect("/lecture-list");
  }

}
