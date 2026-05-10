package com.diy.app.presentation;

import com.diy.app.domain.Lecture;
import com.diy.framework.web.beans.annotations.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(url = "/lectures")
public class LectureAbstractController {
    private final LectureController lectureController;

    public LectureAbstractController(LectureController lectureController) {
        this.lectureController = lectureController;
    }

    public ModelAndView handleRequest(String method, Map<String, ?> params) throws IllegalArgumentException {
        System.out.println("AbstractController handleRequest method : " + method);

        switch (method) {
            case "GET" : return doGet(params);
            case "POST" : return doPost(params);
            case "PUT" : return doPut(params);
            case "DELETE" : return doDelete(params);
            default : throw new IllegalArgumentException("Method not supported");
        }
    }

    public ModelAndView doGet(Map<String, ?> params) {
        List<Lecture> lectures = lectureController.getLectures();
        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);
        return new ModelAndView("lecture-list", model);
    }

    public ModelAndView doPost(Map<String, ?> params) {
        LectureRequest request = LectureRequest.from(params);
        lectureController.addLecture(request);
        return new ModelAndView("redirect:/lectures");
    }

    public ModelAndView doPut(Map<String, ?> params) {
        LectureRequest request = LectureRequest.from(params);
        lectureController.updateLecture(request);
        return new ModelAndView("none");
    }

    public ModelAndView doDelete(Map<String, ?> params) {
        String lectureId = Arrays.toString((String[])params.get("lectureId")).replace("[", "").replace("]", "");
        lectureController.deleteLecture(lectureId);
        return new ModelAndView("none");
    }
}
