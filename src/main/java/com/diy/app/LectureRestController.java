package com.diy.app;

import com.diy.framework.context.annotation.RestController;
import com.diy.framework.web.mvc.anotation.RequestMapping;
import com.diy.framework.web.mvc.anotation.RequestMethod;

@RestController
public class LectureRestController {

    private final LectureService lectureService;

    public LectureRestController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(value = "/lectures", methods = {RequestMethod.GET})
    public Lecture getLecture(String id) {
        return lectureService.getLectures().stream().filter(lecture -> lecture.getId().equals(Long.parseLong(id))).findFirst().orElse(null);
    }
}
