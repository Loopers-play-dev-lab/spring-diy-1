package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;
import com.diy.framework.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public ModelAndView doGet(Map<String, ?> params) {
        List<Lecture> lectures = lectureService.getAllLectures();

        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);

        return new ModelAndView("lecture-list", model);
    }

    public ModelAndView doPost(Map<String, ?> params) {
        String name = (String) params.get("name");
        int price = Integer.parseInt((String) params.get("price"));
        CreateLectureRequest createRequest = new CreateLectureRequest(name, price);

        lectureService.createLecture(createRequest);

        return new ModelAndView("redirect:/lectures");
    }
}
