package com.diy.app;

import com.diy.framework.web.mvc.ModelAndView;

import java.math.BigDecimal;
import java.util.Map;

public class LectureController {
    private final LectureService lectureService;

    public LectureController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public ModelAndView list(final Map<String, ?> params) {
        return new ModelAndView("lecture-list", Map.of("lectures", lectureService.getLectures()));
    }

    public ModelAndView create(final Map<String, ?> params) {
        Lecture lecture = Lecture.register((String) params.get("name"), new BigDecimal((String) params.get("price")));
        lectureService.registerLecture(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    public ModelAndView update(final Map<String, ?> params) {
        int id = Integer.parseInt((String) params.get("id"));
        Lecture lecture = lectureService.findById(id);
        if (lecture != null) {
            lecture.updateName((String) params.get("name"));
            lecture.updatePrice(new BigDecimal((String) params.get("price")));
        }
        return new ModelAndView("redirect:/lectures");
    }

    public ModelAndView delete(final Map<String, ?> params) {
        int id = Integer.parseInt((String) params.get("id"));
        lectureService.deleteById(id);
        return new ModelAndView("redirect:/lectures");
    }
}
