package com.diy.app;

import com.diy.app.lecture.LectureRepository;
import com.diy.app.lecture.LectureService;
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
    protected void initHandlerMappings(final HandlerMapping handlerMapping) {
        LectureRepository lectureRepository = new LectureRepository();
        LectureService lectureService = new LectureService(lectureRepository);

        handlerMapping.setMapping("GET", "/lectures", new LectureListController(lectureService));
        handlerMapping.setMapping("POST", "/lectures", new LectureCreateController(lectureService));
        handlerMapping.setMapping("PUT", "/lectures", new LectureUpdateController(lectureService));
        handlerMapping.setMapping("DELETE", "/lectures", new LectureDeleteController(lectureService));
    }
}
