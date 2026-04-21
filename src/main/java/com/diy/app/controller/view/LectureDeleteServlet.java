package com.diy.app.controller.view;

import com.diy.app.store.Lecture;
import com.diy.app.store.LectureStore;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lecture-delete/*")
public class LectureDeleteServlet extends HttpServlet {

  private final LectureStore lectureStore = new LectureStore();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Lecture lecture = lectureStore.get(getId(req));
    if (lecture == null) throw new IllegalArgumentException("존재하지 않는 강의입니다.");

    req.setAttribute("lecture", lecture);
    RequestDispatcher dispatcher = req.getRequestDispatcher("/lecture-delete.jsp");
    dispatcher.forward(req, resp);
  }

  private Long getId(HttpServletRequest req) {
    String path = req.getPathInfo();
    if (path == null || path.length() <= 1) throw new IllegalArgumentException("잘못된 ID입니다.");

    try {
      return Long.parseLong(path.substring(1));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("유효한 ID값을 입력해야 합니다.");
    }
  }

}
