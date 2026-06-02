package com.diy.app.lecture;

import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;

@Component
public class LectureConfig {

    private final LectureService lectureService;

    public LectureConfig(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Bean("/lectures/v1")
    public  LectureController lectureController() {
        return new LectureController(lectureService);
    }

}
