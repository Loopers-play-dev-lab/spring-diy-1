package com.diy.app;

import com.diy.app.lecture.controller.LectureCreateController;
import com.diy.app.lecture.controller.LectureDeleteController;
import com.diy.app.lecture.controller.LectureListController;
import com.diy.app.lecture.controller.LectureUpdateController;
import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.HandlerMapping;
import javax.servlet.annotation.WebServlet;

@WebServlet("/")
public class LecturesDispatcherServlet extends DispatcherServlet {

    @Override
    protected void initHandlerMappings(HandlerMapping handlerMapping) {
        handlerMapping.setMapping("GET", "/lectures", new LectureListController());
        handlerMapping.setMapping("POST", "/lectures", new LectureCreateController());
        handlerMapping.setMapping("PUT", "/lectures", new LectureUpdateController());
        handlerMapping.setMapping("DELETE", "/lectures", new LectureDeleteController());
    }
}
