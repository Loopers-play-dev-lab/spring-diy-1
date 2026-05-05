package com.diy.framework.web.server.controller;

import com.diy.app.controller.api.LectureController;
import com.diy.app.controller.view.LectureListController;
import com.diy.framework.web.server.exceptions.NotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ControllerResolver {

  private final Map<String, Controller> controllerMap;

  public ControllerResolver(
      LectureController lectureController,
      LectureListController lectureListController
  ) {
    controllerMap = new HashMap<>();
    controllerMap.put("/lectures", lectureController);
    controllerMap.put("/lecture-list", lectureListController);
  }

  public Controller resolve(String path) {
    var controller = controllerMap.get(path);
    if (controller == null) {
      throw new NotFoundException();
    }
    return controller;
  }

}
