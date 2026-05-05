package com.diy.app;

import com.diy.framework.web.annotation.Autowired;
import com.diy.framework.web.annotation.Component;

@Component
public class LectureService {

    private final Lecture lecture;

    @Autowired
    public LectureService(final Lecture lecture) {
        this.lecture = lecture;
    }

    public Lecture getLecture() {
        return lecture;
    }
}