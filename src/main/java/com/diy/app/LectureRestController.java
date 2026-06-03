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

    @RequestMapping(value = "/lecture", methods = {RequestMethod.GET})
    public String getLecture() {
        return new String("hello rest");
    }
}
