package com.diy.app;

import com.diy.framework.context.annotation.RestController;
import com.diy.framework.web.mvc.anotation.RequestMapping;
import com.diy.framework.web.mvc.anotation.RequestMethod;
import java.util.Collection;

@RestController
public class LectureControllerV3 {

    private final LectureService lectureService;

    public LectureControllerV3(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(value = "/lectures/v3", methods = {RequestMethod.GET})
    private LecturesResponse getLectures() {
        return new LecturesResponse(lectureService.getLectures());
    }

    public record LecturesResponse(
        Collection<Lecture> lectures
    ) { }
}
