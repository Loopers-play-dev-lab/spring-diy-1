package com.diy.app.controller.view;

import com.diy.app.store.Lecture;
import com.diy.app.store.LectureStore;
import com.diy.framework.web.server.exceptions.MethodNotAllowedException;
import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.servlet.views.ModelAndView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureListController implements Controller {

  private final LectureStore lectureStore = new LectureStore();

  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    return switch (request.getMethod()) {
      case "GET" -> findAll();
      default -> throw new MethodNotAllowedException();
    };
  }

  public ModelAndView findAll() {
     List<Lecture> lectures = lectureStore.list();

    Map<String, Object> model = new HashMap<>();
    model.put("lectures", lectures);
    return new ModelAndView("lecture-list", model);
  }
}
