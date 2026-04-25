package com.diy.app.controller.view;

import com.diy.app.store.Lecture;
import com.diy.app.store.LectureStore;
import com.diy.framework.web.server.exceptions.MethodNotAllowedException;
import com.diy.framework.web.server.interfaces.Controller;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureListController implements Controller {

  private final LectureStore lectureStore = new LectureStore();

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
     RequestDispatcher dispatcher = req.getRequestDispatcher("/lecture-list.jsp");
     dispatcher.forward(req, resp);
  }
}
