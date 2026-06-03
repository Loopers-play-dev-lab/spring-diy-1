package com.diy.app;

import com.diy.framework.web.mvc.anotation.RequestMapping;
import com.diy.framework.web.mvc.anotation.RequestMethod;
import com.diy.framework.web.mvc.anotation.RestController;

import java.util.Collection;

@RestController
public class LectureControllerV3 {

    private final LectureService lectureService;

    public LectureControllerV3(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(value = "/api/lectures", methods = {RequestMethod.POST})
    private Lecture createLecture(final Lecture lecture) {
        lectureService.registerLecture(lecture);
        return lecture;
    }

    @RequestMapping(value = "/api/lectures", methods = {RequestMethod.GET})
    private Collection<Lecture> getLectures() {
        return lectureService.getLectures();
    }
}
