package com.diy.framework.web.server;

import com.diy.app.controller.api.LectureController;
import com.diy.app.controller.view.LectureListController;
import com.diy.framework.web.server.exceptions.NotFoundException;
import com.diy.framework.web.server.interfaces.Controller;
import java.util.HashMap;
import java.util.Map;

public class ControllerResolver {
  private final Map<String, Controller> controllerMap;

  public ControllerResolver() {
    controllerMap = new HashMap<>();
    controllerMap.put("/lectures", new LectureController());
    controllerMap.put("/lecture-list", new LectureListController());
  }

  public Controller find(String path) {
    var controller = controllerMap.get(path);
    if (controller == null) throw new NotFoundException();
    return controller;
  }

}
