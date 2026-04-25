package com.diy.app.presentation;

import com.diy.app.domain.Lecture;
import com.diy.config.AppConfig;
import com.diy.framework.web.servlet.AbstractController;
import com.diy.framework.web.view.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LectureAbstractController extends AbstractController {
    private final LectureController lectureController;
    private static LectureAbstractController instance;

    private LectureAbstractController() {
        this.lectureController = AppConfig.lectureController();
    }

    public static LectureAbstractController getInstance() {
        if (instance == null) {
            instance = new LectureAbstractController();
        }
        return instance;
    }

    @Override
    public ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<Lecture> lectures = lectureController.getLectures();
        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);
        return new ModelAndView("lecture-list", model);
    }

    @Override
    public ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LectureRequest request = LectureRequest.from(req.getAttribute("params"));
        lectureController.addLecture(request);
        return new ModelAndView("redirect:/lectures");
    }

    @Override
    public ModelAndView doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LectureRequest request = LectureRequest.from(req.getAttribute("params"));
        lectureController.updateLecture(request);
        return new ModelAndView("");
    }

    @Override
    public ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String lectureId = req.getParameter("lectureId");
        lectureController.deleteLecture(lectureId);
        return new ModelAndView("");
    }
}
