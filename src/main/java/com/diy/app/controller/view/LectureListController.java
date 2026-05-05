package com.diy.app.controller.view;

import com.diy.app.repository.Lecture;
import com.diy.app.repository.LectureRepository;
import com.diy.framework.web.annotations.Component;
import com.diy.framework.web.server.exceptions.MethodNotAllowedException;
import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.servlet.views.ModelAndView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LectureListController implements Controller {

  private final LectureRepository lectureRepository = new LectureRepository();

  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    return switch (request.getMethod()) {
      case "GET" -> findAll();
      default -> throw new MethodNotAllowedException();
    };
  }

  public ModelAndView findAll() {
     List<Lecture> lectures = lectureRepository.list();

    Map<String, Object> model = new HashMap<>();
    model.put("lectures", lectures);
    return new ModelAndView("lecture-list", model);
  }
}
