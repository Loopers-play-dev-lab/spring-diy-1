package com.diy.framework.web.server.controller;

import com.diy.app.controller.api.LectureController;
import com.diy.app.controller.view.LectureListController;
import com.diy.framework.web.server.exceptions.NotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ControllerResolver {
  private final Map<String, Controller> controllerMap;

  public ControllerResolver() {
    controllerMap = new HashMap<>();
    controllerMap.put("/lectures", new LectureController());
    controllerMap.put("/lecture-list", new LectureListController());
  }

  public Controller resolve(String path) {
    var controller = controllerMap.get(path);
    if (controller == null) throw new NotFoundException();
    return controller;
  }

}
