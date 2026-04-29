package com.diy.app.presentation;

import com.diy.app.domain.Lecture;
import com.diy.config.AppConfig;
import com.diy.framework.web.mvc.servlet.AbstractController;
import com.diy.framework.web.mvc.view.ModelAndView;

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
    public ModelAndView doGet(Map<String, ?> params) {
        List<Lecture> lectures = lectureController.getLectures();
        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);
        return new ModelAndView("lecture-list", model);
    }

    @Override
    public ModelAndView doPost(Map<String, ?> params) {
        LectureRequest request = LectureRequest.from(params);
        lectureController.addLecture(request);
        return new ModelAndView("redirect:/lectures");
    }

    @Override
    public ModelAndView doPut(Map<String, ?> params) {
        LectureRequest request = LectureRequest.from(params);
        lectureController.updateLecture(request);
        return new ModelAndView("");
    }

    @Override
    public ModelAndView doDelete(Map<String, ?> params) {
        String lectureId = (String) params.get("lectureId");
        lectureController.deleteLecture(lectureId);
        return new ModelAndView("");
    }
}
