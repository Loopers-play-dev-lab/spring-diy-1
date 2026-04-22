package com.diy.app.controller.view;

import com.diy.app.store.Lecture;
import com.diy.app.store.LectureStore;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lecture-list")
public class LectureListServlet extends HttpServlet {

  private final LectureStore lectureStore = new LectureStore();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     List<Lecture> lectures = lectureStore.list();

     req.setAttribute("lectures", lectures);
     RequestDispatcher dispatcher = req.getRequestDispatcher("/lecture-list.jsp");
     dispatcher.forward(req, resp);
  }

}
